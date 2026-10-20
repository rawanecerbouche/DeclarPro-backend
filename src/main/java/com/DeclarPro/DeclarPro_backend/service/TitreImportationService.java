package com.DeclarPro.DeclarPro_backend.service;

import com.DeclarPro.DeclarPro_backend.dto.*;
import com.DeclarPro.DeclarPro_backend.entity.*;
import com.DeclarPro.DeclarPro_backend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TitreImportationService {

    private static final Set<String> INCOTERMS_CIF_CIP = Set.of("CIF", "CIP");
    private static final Set<String> INCOTERMS_DEPART = Set.of("FOB", "EXW", "FCA");

    private final TitreImportationRepository titreRepository;
    private final ImportateurRepository importateurRepository;
    private final PortEntreeRepository portEntreeRepository;
    private final RegleCoherenceRepository regleCoherenceRepository;
    private final AlerteRepository alerteRepository;
    private final CalculFraisRepository calculFraisRepository;

    public TitreImportationService(TitreImportationRepository titreRepository,
                                    ImportateurRepository importateurRepository,
                                    PortEntreeRepository portEntreeRepository,
                                    RegleCoherenceRepository regleCoherenceRepository,
                                    AlerteRepository alerteRepository,
                                    CalculFraisRepository calculFraisRepository) {
        this.titreRepository = titreRepository;
        this.importateurRepository = importateurRepository;
        this.portEntreeRepository = portEntreeRepository;
        this.regleCoherenceRepository = regleCoherenceRepository;
        this.alerteRepository = alerteRepository;
        this.calculFraisRepository = calculFraisRepository;
    }

    @Transactional(readOnly = true)
    public TitreRapportDTO getRapport(Long id) {
        TitreImportation titre = titreRepository.findById(id)
                .orElseThrow(() -> notFound("TitreImportation", id));
        BigDecimal valeurEnDouane = calculateValeurEnDouane(titre);
        List<Alerte> alertes = alerteRepository.findByTitreImportation_Id(id);
        List<CalculFrais> calculsFrais = calculFraisRepository.findByTitreImportation_IdOrderByDateCreationDescIdDesc(id);
        return TitreMapper.toRapportDTO(titre, valeurEnDouane, alertes, calculsFrais);
    }

    @Transactional
    public TitreResponse createTitre(TitreCreateRequest request) {
        Importateur importateur = resolveImportateur(request.importateur());

        TitreImportation titre = new TitreImportation();
        titre.setImportateur(importateur);
        titre.setNumeroEnregistrement(
                (request.numeroEnregistrement() == null || request.numeroEnregistrement().isBlank())
                        ? generateNumeroEnregistrement()
                        : request.numeroEnregistrement());
        titre.setCategorieTitre(request.categorieTitre());
        titre.setEtat("NOUVEAU");
        titre.setExpediteur(request.expediteur());
        titre.setDevise(request.devise());
        titre.setModalitePaiement(request.modalitePaiement());
        titre.setRegimeDouanier(request.regimeDouanier());
        titre.setBureauDouanier(request.bureauDouanier());
        titre.setIncoterm(request.incoterm());
        titre.setObservations(request.observations());
        titre.setDateCreation(LocalDate.now());

        if (request.portEntreeId() != null) {
            titre.setPortEntree(portEntreeRepository.findById(request.portEntreeId())
                    .orElseThrow(() -> notFound("PortEntree", request.portEntreeId())));
        }

        if (request.domiciliation() != null) {
            titre.setDomiciliation(buildDomiciliation(request.domiciliation()));
        }

        if (request.marchandises() != null) {
            request.marchandises().forEach(m -> titre.addMarchandise(buildMarchandise(m)));
        }
        if (request.factures() != null) {
            request.factures().forEach(f -> titre.addFacture(buildFacture(f)));
        }
        if (request.documents() != null) {
            request.documents().forEach(d -> titre.addDocumentJoint(buildDocumentJoint(d)));
        }

        TitreImportation saved = titreRepository.save(titre);

        BigDecimal valeurEnDouane = calculateValeurEnDouane(saved);
        List<Alerte> alertes = runCoherenceRules(saved, valeurEnDouane);

        return TitreMapper.toDTO(saved, valeurEnDouane, alertes);
    }

    @Transactional
    public TitreResponse updateTitre(Long id, TitreCreateRequest request) {
        TitreImportation titre = titreRepository.findById(id)
                .orElseThrow(() -> notFound("TitreImportation", id));

        titre.setCategorieTitre(request.categorieTitre());
        titre.setExpediteur(request.expediteur());
        titre.setDevise(request.devise());
        titre.setModalitePaiement(request.modalitePaiement());
        titre.setRegimeDouanier(request.regimeDouanier());
        titre.setBureauDouanier(request.bureauDouanier());
        titre.setIncoterm(request.incoterm());
        titre.setObservations(request.observations());
        if (titre.getEtat() == null || titre.getEtat().equals("NOUVEAU")) {
            titre.setEtat("MODIFIE");
        }

        if (request.portEntreeId() != null) {
            titre.setPortEntree(portEntreeRepository.findById(request.portEntreeId())
                    .orElseThrow(() -> notFound("PortEntree", request.portEntreeId())));
        }

        if (request.domiciliation() != null) {
            if (titre.getDomiciliation() != null) {
                applyDomiciliation(titre.getDomiciliation(), request.domiciliation());
            } else {
                titre.setDomiciliation(buildDomiciliation(request.domiciliation()));
            }
        }

        if (request.marchandises() != null) {
            new ArrayList<>(titre.getMarchandises()).forEach(titre::removeMarchandise);
            request.marchandises().forEach(m -> titre.addMarchandise(buildMarchandise(m)));
        }
        if (request.factures() != null) {
            new ArrayList<>(titre.getFactures()).forEach(titre::removeFacture);
            request.factures().forEach(f -> titre.addFacture(buildFacture(f)));
        }

        TitreImportation saved = titreRepository.save(titre);

        alerteRepository.deleteByTitreImportation_Id(saved.getId());
        BigDecimal valeurEnDouane = calculateValeurEnDouane(saved);
        List<Alerte> alertes = runCoherenceRules(saved, valeurEnDouane);

        return TitreMapper.toDTO(saved, valeurEnDouane, alertes);
    }

    @Transactional(readOnly = true)
    public List<TitreResponse> listTitres(String etat, String numeroEnregistrement) {
        List<TitreImportation> titres;
        if (numeroEnregistrement != null && !numeroEnregistrement.isBlank()) {
            titres = titreRepository.findByNumeroEnregistrementContainingIgnoreCase(numeroEnregistrement);
        } else if (etat != null && !etat.isBlank()) {
            titres = titreRepository.findByEtat(etat);
        } else {
            titres = titreRepository.findAll();
        }
        return titres.stream()
                .map(t -> TitreMapper.toDTO(t, calculateValeurEnDouane(t), alerteRepository.findByTitreImportation_Id(t.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public TitreResponse getTitre(Long id) {
        TitreImportation titre = titreRepository.findById(id)
                .orElseThrow(() -> notFound("TitreImportation", id));
        BigDecimal valeurEnDouane = calculateValeurEnDouane(titre);
        List<Alerte> alertes = alerteRepository.findByTitreImportation_Id(id);
        return TitreMapper.toDTO(titre, valeurEnDouane, alertes);
    }

    /**
     * FOB + Fret + Assurance + Accessoires, tel que défini par le cahier des charges (S1/S5).
     */
    public BigDecimal calculateValeurEnDouane(TitreImportation titre) {
        Domiciliation d = titre.getDomiciliation();
        if (d == null) return BigDecimal.ZERO;
        return nz(d.getMontantFob()).add(nz(d.getMontantFret())).add(nz(d.getMontantAssurance())).add(nz(d.getMontantAccessoires()));
    }

    /**
     * Évalue les 6 règles de cohérence actives lues depuis regle_coherence (dimension/opérateur/valeur_reference),
     * sans dupliquer les seuils/messages en dur côté Java. RC-006 (ratio valeur/poids par code SH) nécessite une
     * table de bornes par code SH qui n'existe pas encore côté backend ; elle est donc évaluée côté frontend
     * (RATIOS_PAR_SH) en attendant que cette donnée référentielle soit ajoutée en base.
     */
    @Transactional
    public List<Alerte> runCoherenceRules(TitreImportation titre, BigDecimal valeurEnDouane) {
        List<Alerte> created = new ArrayList<>();
        for (RegleCoherence regle : regleCoherenceRepository.findByActiveTrue()) {
            Alerte alerte = switch (regle.getDimension()) {
                case "assurance" -> evaluateAssurance(titre, regle);
                case "fret_assurance" -> evaluateFretAssurance(titre, regle);
                case "valeur_douane" -> evaluateValeurDouane(titre, regle, valeurEnDouane);
                case "devise" -> evaluateDevise(titre, regle);
                case "poids_net" -> evaluatePoidsNet(titre, regle);
                default -> null; // ratio_valeur_poids (RC-006) et dimensions inconnues : non évaluables ici
            };
            if (alerte != null) {
                alerte.setTitreImportation(titre);
                alerte.setRegleCoherence(regle);
                alerte.setDateCreation(LocalDate.now());
                created.add(alerteRepository.save(alerte));
            }
        }
        return created;
    }

    // --- RC-001 : incoterm CIF/CIP => assurance > 0 ---
    private Alerte evaluateAssurance(TitreImportation titre, RegleCoherence regle) {
        if (!INCOTERMS_CIF_CIP.contains(titre.getIncoterm())) return null;
        BigDecimal assurance = titre.getDomiciliation() != null ? nz(titre.getDomiciliation().getMontantAssurance()) : BigDecimal.ZERO;
        if (compare(assurance, regle.getOperateur(), parseNumeric(regle.getValeurReference()))) return null;
        return buildAlerte(regle, assurance.toPlainString(), regle.getOperateur() + " " + regle.getValeurReference());
    }

    // --- RC-002 : incoterm FOB/EXW/FCA mais fret ou assurance > 0 ---
    private Alerte evaluateFretAssurance(TitreImportation titre, RegleCoherence regle) {
        if (!INCOTERMS_DEPART.contains(titre.getIncoterm())) return null;
        BigDecimal fret = titre.getDomiciliation() != null ? nz(titre.getDomiciliation().getMontantFret()) : BigDecimal.ZERO;
        BigDecimal assurance = titre.getDomiciliation() != null ? nz(titre.getDomiciliation().getMontantAssurance()) : BigDecimal.ZERO;
        if (fret.signum() <= 0 && assurance.signum() <= 0) return null;
        return buildAlerte(regle, "fret=" + fret + ", assurance=" + assurance, "fret = 0 et assurance = 0");
    }

    // --- RC-003 : écart |factures - valeurEnDouane| / valeurEnDouane > 10% ---
    private Alerte evaluateValeurDouane(TitreImportation titre, RegleCoherence regle, BigDecimal valeurEnDouane) {
        if (valeurEnDouane == null || valeurEnDouane.signum() == 0 || titre.getFactures().isEmpty()) return null;
        BigDecimal sumFactures = titre.getFactures().stream().map(f -> nz(f.getMontant())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ecart = sumFactures.subtract(valeurEnDouane).abs().divide(valeurEnDouane, 6, java.math.RoundingMode.HALF_UP);
        BigDecimal seuil = parsePercent(regle.getValeurReference());
        if (compare(ecart, regle.getOperateur(), seuil)) return null;
        return buildAlerte(regle, ecart.multiply(BigDecimal.valueOf(100)).setScale(1, java.math.RoundingMode.HALF_UP) + "%",
                seuil.multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString() + "%");
    }

    // --- RC-004 : devise d'une facture != devise du titre ---
    private Alerte evaluateDevise(TitreImportation titre, RegleCoherence regle) {
        List<String> mismatched = titre.getFactures().stream()
                .filter(f -> f.getDevise() != null && !f.getDevise().isBlank() && !f.getDevise().equals(titre.getDevise()))
                .map(f -> f.getReference() + "=" + f.getDevise())
                .toList();
        if (mismatched.isEmpty()) return null;
        return buildAlerte(regle, String.join(", ", mismatched), titre.getDevise());
    }

    // --- RC-005 : poids_net > poids_brut d'une marchandise ---
    private Alerte evaluatePoidsNet(TitreImportation titre, RegleCoherence regle) {
        List<String> offending = titre.getMarchandises().stream()
                .filter(m -> m.getPoidsBrut() != null && m.getPoidsNet() != null && m.getPoidsNet().compareTo(m.getPoidsBrut()) > 0)
                .map(m -> (m.getCodeSh() != null ? m.getCodeSh() : m.getDesignation()) + " (net=" + m.getPoidsNet() + ", brut=" + m.getPoidsBrut() + ")")
                .toList();
        if (offending.isEmpty()) return null;
        return buildAlerte(regle, String.join(", ", offending), "poids_net <= poids_brut");
    }

    private Alerte buildAlerte(RegleCoherence regle, String valeurDetectee, String valeurAttendue) {
        Alerte alerte = new Alerte();
        alerte.setSeverite(regle.getSeverite());
        alerte.setMessage(regle.getMessage());
        alerte.setDimension(regle.getDimension());
        alerte.setValeurDetectee(valeurDetectee);
        alerte.setValeurAttendue(valeurAttendue);
        alerte.setScorePenalite(regle.getScorePenalite());
        return alerte;
    }

    // --- helpers ---

    private Importateur resolveImportateur(ImportateurDTO dto) {
        Importateur importateur = (dto.ifu() != null && !dto.ifu().isBlank())
                ? importateurRepository.findByIfu(dto.ifu()).orElseGet(Importateur::new)
                : (dto.id() != null ? importateurRepository.findById(dto.id()).orElseGet(Importateur::new) : new Importateur());

        importateur.setDesciption(dto.description());
        importateur.setCentreRc(dto.centreRc());
        importateur.setIfu(dto.ifu());
        importateur.setNumeroEnregistrement(dto.numeroEnregistrement());
        importateur.setNumeroRc(dto.numeroRc());
        importateur.setCodePin(dto.codePin());
        importateur.setTaxeProfessionnelle(dto.taxeProfessionnelle());
        importateur.setFichierDce(dto.fichierDce());
        importateur.setTelephoneMobile(dto.telephoneMobile());
        importateur.setTelephoneFixe(dto.telephoneFixe());
        importateur.setEmail(dto.email());
        importateur.setAdresse(dto.adresse());
        importateur.setVoie(dto.voie());
        importateur.setCodePostal(dto.codePostal());
        importateur.setLocalite(dto.localite());
        return importateurRepository.save(importateur);
    }

    private Domiciliation buildDomiciliation(DomiciliationDTO dto) {
        Domiciliation d = new Domiciliation();
        applyDomiciliation(d, dto);
        return d;
    }

    private void applyDomiciliation(Domiciliation d, DomiciliationDTO dto) {
        d.setMontantGlobalFactures(dto.montantGlobalFactures());
        d.setMontantFob(dto.montantFob());
        d.setMontantAssurance(dto.montantAssurance());
        d.setMontantFret(dto.montantFret());
        d.setMontantAccessoires(dto.montantAccessoires());
        d.setRib(dto.rib());
        d.setCodeBanque(dto.codeBanque());
        d.setNomBanque(dto.nomBanque());
        d.setCodeGuichet(dto.codeGuichet());
        d.setNomGuichet(dto.nomGuichet());
    }

    private Marchandise buildMarchandise(MarchandiseDTO dto) {
        Marchandise m = new Marchandise();
        m.setDesignation(dto.designation());
        m.setCodeSh(dto.codeSh());
        m.setPoidsNet(dto.poidsNet());
        m.setPoidsBrut(dto.poidsBrut());
        m.setPaysOrigine(dto.paysOrigine());
        m.setCategorieProduit(dto.categorieProduit());
        m.setUnite(dto.unite());
        m.setQuantite(dto.quantite());
        m.setUniteCompLiberele(dto.uniteCompLiberele());
        return m;
    }

    private Facture buildFacture(FactureDTO dto) {
        Facture f = new Facture();
        f.setReference(dto.reference());
        f.setMontant(dto.montant());
        f.setDevise(dto.devise());
        f.setDate(dto.date());
        f.setFichier(dto.fichier());
        return f;
    }

    private DocumentJoint buildDocumentJoint(DocumentJointDTO dto) {
        DocumentJoint d = new DocumentJoint();
        d.setType(dto.type());
        d.setDescription(dto.description());
        d.setDateDocument(dto.dateDocument());
        d.setFichier(dto.fichier());
        return d;
    }

    private String generateNumeroEnregistrement() {
        long year = LocalDate.now().getYear();
        long random = 1_000_000_000L + (long) (Math.random() * 9_000_000_000L);
        return "" + year + random;
    }

    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private static BigDecimal parseNumeric(String s) {
        try {
            return new BigDecimal(s.trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal parsePercent(String s) {
        String trimmed = s.trim();
        if (trimmed.endsWith("%")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        try {
            return new BigDecimal(trimmed).divide(BigDecimal.valueOf(100));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * true si actual satisfait operateur reference (donc AUCUNE alerte), false si la règle est violée.
     */
    private static boolean compare(BigDecimal actual, String operateur, BigDecimal reference) {
        int cmp = actual.compareTo(reference);
        return switch (operateur) {
            case ">" -> cmp > 0;
            case ">=" -> cmp >= 0;
            case "<" -> cmp < 0;
            case "<=" -> cmp <= 0;
            case "=" -> cmp == 0;
            case "!=" -> cmp != 0;
            default -> true;
        };
    }

    private static ResponseStatusException notFound(String entity, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " " + id + " introuvable");
    }
}
