package com.DeclarPro.DeclarPro_backend.service;

import com.DeclarPro.DeclarPro_backend.dto.ChecklistItemDTO;
import com.DeclarPro.DeclarPro_backend.dto.TitreChecklistItemDTO;
import com.DeclarPro.DeclarPro_backend.entity.ChecklistLigne;
import com.DeclarPro.DeclarPro_backend.entity.DocumentReferentiel;
import com.DeclarPro.DeclarPro_backend.entity.Marchandise;
import com.DeclarPro.DeclarPro_backend.entity.RegleDocumentaire;
import com.DeclarPro.DeclarPro_backend.entity.TitreImportation;
import com.DeclarPro.DeclarPro_backend.repository.ChecklistLigneRepository;
import com.DeclarPro.DeclarPro_backend.repository.RegleDocumentaireRepository;
import com.DeclarPro.DeclarPro_backend.repository.TitreImportationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service 2 - "Checklist documentaire intelligente".
 *
 * regle_documentaire mixes two vocabularies in its `dimension` column (see data.sql):
 *  - legacy rows (ids 1-11):  delai, montant, categorie, global, produit_soumis_licence,
 *    produit_soumis_declaration_prealable, exoneration_demandee, produit_sensible
 *  - newer rows (ids 12-25):  MODALITE_PAIEMENT, REGIME_DOUANIER, CODE_SH, CATEGORIE_PRODUIT
 *
 * Two entry points share the same matching engine:
 *  - generateChecklist(...): stateless, ad-hoc preview used by the manual S2Checklist form
 *    (GET /api/checklist). Returns one item per document (motifs from multiple matching
 *    rules are merged), nothing is persisted.
 *  - generateAndPersistChecklistForTitre(titreId): analyzes an existing TitreImportation (S1)
 *    by running the same engine once per Marchandise (using the titre's shared regimeDouanier
 *    and each marchandise's own codeSh/categorieProduit/paysOrigine), unions the results, and
 *    upserts one checklist_ligne row per (document, matching rule) so the reasoning stays
 *    auditable. Existing statut/dateCreation is preserved on regeneration; only
 *    obligatoire/commentaire are refreshed.
 *
 * Only dimensions evaluable from the 4 available values (codeSh, categorieProduit,
 * regimeDouanier, paysOrigine) are matched; everything else is deliberately left
 * informational-only rather than guessed (see per-dimension comments in matches()).
 */
@Service
public class ChecklistService {

    private static final Pattern CHAPITRE_PATTERN = Pattern.compile("(?i)chapitre\\s*(\\d+)");
    private static final Pattern SAUF_RANGE_PATTERN = Pattern.compile("(?i)sauf\\s+([0-9.]+)\\s*(?:a|à)\\s*([0-9.]+)");
    private static final Pattern RANGE_PATTERN = Pattern.compile("(?i)^([0-9.]+)\\s*(?:a|à)\\s*([0-9.]+)$");

    // Legacy 'categorie' / newer 'CATEGORIE_PRODUIT' dimensions use free text that does not
    // line up 1:1 with the 6 broad frontend categories. Only this one alias is unambiguous.
    private static final Map<String, String> CATEGORIE_ALIASES = Map.of("ALIMENTAIRE", "agroalimentaire");

    private final RegleDocumentaireRepository regleDocumentaireRepository;
    private final TitreImportationRepository titreImportationRepository;
    private final ChecklistLigneRepository checklistLigneRepository;

    public ChecklistService(RegleDocumentaireRepository regleDocumentaireRepository,
                             TitreImportationRepository titreImportationRepository,
                             ChecklistLigneRepository checklistLigneRepository) {
        this.regleDocumentaireRepository = regleDocumentaireRepository;
        this.titreImportationRepository = titreImportationRepository;
        this.checklistLigneRepository = checklistLigneRepository;
    }

    // --- Manual / stateless preview (GET /api/checklist) ---

    public List<ChecklistItemDTO> generateChecklist(String codeSh, String categorieProduit,
                                                      String regimeDouanier, String paysOrigine) {
        List<RegleDocumentaire> matched = regleDocumentaireRepository.findActiveWithDocumentAndOrganisme().stream()
                .filter(r -> matches(r, codeSh, categorieProduit, regimeDouanier, paysOrigine))
                .toList();
        return buildChecklistItems(matched);
    }

    private List<ChecklistItemDTO> buildChecklistItems(List<RegleDocumentaire> matched) {
        Map<Long, ChecklistItemBuilder> retained = new LinkedHashMap<>();
        for (RegleDocumentaire regle : matched) {
            DocumentReferentiel doc = regle.getDocumentReferentiel();
            retained.computeIfAbsent(doc.getId(), id -> new ChecklistItemBuilder(doc)).addMotif(regle.getDescription());
        }
        return retained.values().stream().map(ChecklistItemBuilder::toDTO).toList();
    }

    // --- Titre-driven, persisted checklist (analyzes S1) ---

    @Transactional
    public List<TitreChecklistItemDTO> generateAndPersistChecklistForTitre(Long titreId) {
        TitreImportation titre = titreImportationRepository.findById(titreId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TitreImportation " + titreId + " introuvable"));

        List<RegleDocumentaire> allRegles = regleDocumentaireRepository.findActiveWithDocumentAndOrganisme();
        List<Marchandise> marchandises = titre.getMarchandises();

        // Union of matches across every marchandise on the titre (a titre may cover several
        // distinct goods, each with its own SH code / origin / category), deduplicated by rule id.
        Map<Long, RegleDocumentaire> matched = new LinkedHashMap<>();
        if (marchandises.isEmpty()) {
            // No goods recorded yet: still surface the always-required global documents.
            allRegles.stream()
                    .filter(r -> matches(r, null, null, titre.getRegimeDouanier(), null))
                    .forEach(r -> matched.put(r.getId(), r));
        } else {
            for (Marchandise m : marchandises) {
                allRegles.stream()
                        .filter(r -> matches(r, m.getCodeSh(), m.getCategorieProduit(), titre.getRegimeDouanier(), m.getPaysOrigine()))
                        .forEach(r -> matched.putIfAbsent(r.getId(), r));
            }
        }

        List<ChecklistLigne> persisted = new ArrayList<>();
        for (RegleDocumentaire regle : matched.values()) {
            ChecklistLigne ligne = checklistLigneRepository
                    .findByTitreImportation_IdAndDocumentReferentiel_IdAndRegleDocumentaire_Id(
                            titreId, regle.getDocumentReferentiel().getId(), regle.getId())
                    .orElseGet(() -> {
                        ChecklistLigne l = new ChecklistLigne();
                        l.setTitreImportation(titre);
                        l.setDocumentReferentiel(regle.getDocumentReferentiel());
                        l.setRegleDocumentaire(regle);
                        l.setStatut("A_OBTENIR");
                        l.setDateCreation(LocalDate.now());
                        return l;
                    });
            ligne.setObligatoire(regle.getObligatoire());
            ligne.setCommentaire(regle.getDescription());
            persisted.add(checklistLigneRepository.save(ligne));
        }

        return persisted.stream().map(ChecklistService::toTitreChecklistItemDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<TitreChecklistItemDTO> getChecklistForTitre(Long titreId) {
        return checklistLigneRepository.findByTitreImportationIdWithDocument(titreId).stream()
                .map(ChecklistService::toTitreChecklistItemDTO)
                .toList();
    }

    private static TitreChecklistItemDTO toTitreChecklistItemDTO(ChecklistLigne ligne) {
        DocumentReferentiel doc = ligne.getDocumentReferentiel();
        return new TitreChecklistItemDTO(
                ligne.getId(),
                doc.getId(),
                doc.getCode(),
                doc.getLibelle(),
                doc.getTypePortnet(),
                new ChecklistItemDTO.OrganismeRefDTO(doc.getOrganisme().getCode(), doc.getOrganisme().getNom()),
                doc.getDelaiJours(),
                doc.getCritique(),
                ligne.getObligatoire(),
                ligne.getStatut(),
                ligne.getRegleDocumentaire() != null ? ligne.getRegleDocumentaire().getDescription() : null
        );
    }

    // --- Matching engine, shared by both entry points ---

    private boolean matches(RegleDocumentaire regle, String codeSh, String categorieProduit,
                             String regimeDouanier, String paysOrigine) {
        String dimension = regle.getDimension() == null ? "" : regle.getDimension().trim();
        String value = regle.getValue() == null ? "" : regle.getValue().trim();

        // Row 14's dimension was written as 'CODE' || '' || 'cl_SH' in data.sql, which
        // concatenates to the literal string "CODEcl_SH" instead of "CODE_SH" (SQL typo in
        // the seed data). Accepting both keeps the asbestos rule (id 14) from silently never
        // matching; data.sql itself is left untouched per instructions.
        if (dimension.equalsIgnoreCase("CODE_SH") || dimension.equalsIgnoreCase("CODEcl_SH")) {
            return matchesCodeSh(value, codeSh);
        }

        if (dimension.equalsIgnoreCase("global")) {
            return "true".equalsIgnoreCase(value);
        }

        if (dimension.equalsIgnoreCase("categorie")) {
            return categorieProduit != null
                    && categorieProduit.equalsIgnoreCase(CATEGORIE_ALIASES.getOrDefault(value.toUpperCase(), value));
        }

        if (dimension.equalsIgnoreCase("REGIME_DOUANIER")) {
            // Straight equality against the rule's raw text. With the frontend's current
            // coded regime values (conso_directe, admission_temp, ...) this never matches
            // rows 13/25, whose values are full legal descriptions ("Regime particulier/
            // suspensif...", "Franchise douaniere") — see report to user.
            return regimeDouanier != null && regimeDouanier.equalsIgnoreCase(value);
        }

        // CATEGORIE_PRODUIT (free-text product descriptions, e.g. "Matieres premieres en
        // polyethylene..."), MODALITE_PAIEMENT (no matching frontend param at all), and the
        // legacy delai / montant / produit_soumis_licence / produit_soumis_declaration_prealable
        // / exoneration_demandee / produit_sensible dimensions (no boolean flags collected by
        // this endpoint) are all informational-only for now: never auto-matched here rather
        // than guessed. paysOrigine is accepted as a parameter but no current rule uses it.
        return false;
    }

    private boolean matchesCodeSh(String ruleValue, String submittedCode) {
        if (ruleValue.isBlank() || submittedCode == null || submittedCode.isBlank()) {
            return false;
        }
        String normalizedSubmitted = digitsOnly(submittedCode);
        for (String alternative : ruleValue.split("\\s*/\\s*")) {
            String alt = alternative.trim();
            if (alt.isEmpty()) continue;
            if (matchesAlternative(alt, normalizedSubmitted)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesAlternative(String alt, String normalizedSubmitted) {
        Matcher chapitreMatcher = CHAPITRE_PATTERN.matcher(alt);
        if (chapitreMatcher.find()) {
            String chapitre = chapitreMatcher.group(1);
            String chapitrePadded = chapitre.length() == 1 ? "0" + chapitre : chapitre;
            if (!normalizedSubmitted.startsWith(chapitrePadded)) {
                return false;
            }
            Matcher saufMatcher = SAUF_RANGE_PATTERN.matcher(alt);
            if (saufMatcher.find()) {
                boolean excluded = isWithinRange(digitsOnly(saufMatcher.group(1)), digitsOnly(saufMatcher.group(2)), normalizedSubmitted);
                return !excluded;
            }
            return true;
        }

        Matcher rangeMatcher = RANGE_PATTERN.matcher(alt);
        if (rangeMatcher.matches()) {
            return isWithinRange(digitsOnly(rangeMatcher.group(1)), digitsOnly(rangeMatcher.group(2)), normalizedSubmitted);
        }

        String normalizedAlt = digitsOnly(alt);
        return !normalizedAlt.isEmpty() && normalizedSubmitted.startsWith(normalizedAlt);
    }

    /** Compares a submitted SH code against a [low, high] bound by truncating everything to the shorter bound's digit length. */
    private boolean isWithinRange(String low, String high, String normalizedSubmitted) {
        int len = Math.min(low.length(), high.length());
        if (len == 0 || normalizedSubmitted.length() < len) {
            return false;
        }
        String truncatedSubmitted = normalizedSubmitted.substring(0, len);
        String truncatedLow = low.substring(0, len);
        String truncatedHigh = high.substring(0, len);
        return truncatedSubmitted.compareTo(truncatedLow) >= 0 && truncatedSubmitted.compareTo(truncatedHigh) <= 0;
    }

    private String digitsOnly(String s) {
        return s.replaceAll("\\D", "");
    }

    private static class ChecklistItemBuilder {
        private final DocumentReferentiel doc;
        private final List<String> motifs = new ArrayList<>();

        ChecklistItemBuilder(DocumentReferentiel doc) {
            this.doc = doc;
        }

        void addMotif(String motif) {
            if (motif != null && !motif.isBlank() && !motifs.contains(motif)) {
                motifs.add(motif);
            }
        }

        ChecklistItemDTO toDTO() {
            return new ChecklistItemDTO(
                    doc.getId(),
                    doc.getCode(),
                    doc.getLibelle(),
                    doc.getTypePortnet(),
                    new ChecklistItemDTO.OrganismeRefDTO(doc.getOrganisme().getCode(), doc.getOrganisme().getNom()),
                    doc.getDelaiJours(),
                    doc.getCritique(),
                    String.join("; ", motifs)
            );
        }
    }
}
