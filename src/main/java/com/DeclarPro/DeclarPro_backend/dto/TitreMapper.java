package com.DeclarPro.DeclarPro_backend.dto;

import com.DeclarPro.DeclarPro_backend.entity.*;

import java.math.BigDecimal;
import java.util.List;

public final class TitreMapper {

    private TitreMapper() {
    }

    public static ImportateurDTO toDTO(Importateur imp) {
        if (imp == null) return null;
        return new ImportateurDTO(imp.getID_importateur(), imp.getDesciption(), imp.getCentreRc(), imp.getIfu(),
                imp.getNumeroEnregistrement(), imp.getNumeroRc(), imp.getCodePin(), imp.getTaxeProfessionnelle(),
                imp.getFichierDce(), imp.getTelephoneMobile(), imp.getTelephoneFixe(), imp.getEmail(),
                imp.getAdresse(), imp.getVoie(), imp.getCodePostal(), imp.getLocalite());
    }

    public static CalculFraisDTO toDTO(CalculFrais c) {
        return new CalculFraisDTO(c.getId(), c.getValeurCif(), c.getDroitDouaneTaux(), c.getMontantDD(),
                c.getTpiTaux(), c.getMontantTPI(), c.getTvaTaux(), c.getBaseTVA(), c.getMontantTVA(),
                c.getTicTaux(), c.getMontantTIC(), c.getBmdaTaux(), c.getMontantBMDA(), c.getTotalEstime(),
                c.getDateCreation());
    }

    public static TitreRapportDTO.PortEntreeRefDTO toDTO(PortEntree p) {
        if (p == null) return null;
        return new TitreRapportDTO.PortEntreeRefDTO(p.getId(), p.getCode(), p.getNom(), p.getVille());
    }

    public static TitreRapportDTO toRapportDTO(TitreImportation t, BigDecimal valeurEnDouane,
                                                List<Alerte> alertes, List<CalculFrais> calculsFrais) {
        return new TitreRapportDTO(
                t.getId(),
                t.getNumeroEnregistrement(),
                t.getCategorieTitre(),
                t.getEtat(),
                t.getExpediteur(),
                t.getDevise(),
                t.getModalitePaiement(),
                t.getRegimeDouanier(),
                t.getBureauDouanier(),
                t.getIncoterm(),
                toDTO(t.getPortEntree()),
                t.getObservations(),
                t.getDateCreation(),
                valeurEnDouane,
                toDTO(t.getImportateur()),
                toDTO(t.getDomiciliation()),
                t.getMarchandises().stream().map(TitreMapper::toDTO).toList(),
                t.getFactures().stream().map(TitreMapper::toDTO).toList(),
                t.getDocumentsJoints().stream().map(TitreMapper::toDTO).toList(),
                alertes.stream().map(TitreMapper::toDTO).toList(),
                calculsFrais.stream().map(TitreMapper::toDTO).toList()
        );
    }

    public static MarchandiseDTO toDTO(Marchandise m) {
        return new MarchandiseDTO(m.getId(), m.getDesignation(), m.getCodeSh(), m.getPoidsNet(),
                m.getPoidsBrut(), m.getPaysOrigine(), m.getCategorieProduit(), m.getUnite(), m.getQuantite(), m.getUniteCompLiberele());
    }

    public static FactureDTO toDTO(Facture f) {
        return new FactureDTO(f.getId(), f.getReference(), f.getMontant(), f.getDevise(), f.getDate(), f.getFichier());
    }

    public static DocumentJointDTO toDTO(DocumentJoint d) {
        return new DocumentJointDTO(d.getId(), d.getType(), d.getDescription(), d.getDateDocument(), d.getFichier());
    }

    public static DomiciliationDTO toDTO(Domiciliation d) {
        if (d == null) return null;
        return new DomiciliationDTO(d.getId(), d.getMontantGlobalFactures(), d.getMontantFob(),
                d.getMontantAssurance(), d.getMontantFret(), d.getMontantAccessoires(), d.getRib(),
                d.getCodeBanque(), d.getNomBanque(), d.getCodeGuichet(), d.getNomGuichet());
    }

    public static AlerteDTO toDTO(Alerte a) {
        return new AlerteDTO(a.getId(), a.getRegleCoherence() != null ? a.getRegleCoherence().getCode() : null,
                a.getSeverite(), a.getMessage(), a.getDimension(), a.getValeurDetectee(), a.getValeurAttendue(),
                a.getScorePenalite(), a.getDateCreation());
    }

    public static RegleCoherenceDTO toDTO(RegleCoherence r) {
        return new RegleCoherenceDTO(r.getId(), r.getCode(), r.getLibelle(), r.getDimension(), r.getOperateur(),
                r.getValeurReference(), r.getSeverite(), r.getScorePenalite(), r.getMessage());
    }

    public static TitreResponse toDTO(TitreImportation t, BigDecimal valeurEnDouane, List<Alerte> alertes) {
        return new TitreResponse(
                t.getId(),
                t.getImportateur() != null ? t.getImportateur().getID_importateur() : null,
                t.getNumeroEnregistrement(),
                t.getCategorieTitre(),
                t.getEtat(),
                t.getExpediteur(),
                t.getDevise(),
                t.getModalitePaiement(),
                t.getRegimeDouanier(),
                t.getBureauDouanier(),
                t.getIncoterm(),
                t.getPortEntree() != null ? t.getPortEntree().getId() : null,
                t.getObservations(),
                t.getDateCreation(),
                valeurEnDouane,
                toDTO(t.getDomiciliation()),
                t.getMarchandises().stream().map(TitreMapper::toDTO).toList(),
                t.getFactures().stream().map(TitreMapper::toDTO).toList(),
                t.getDocumentsJoints().stream().map(TitreMapper::toDTO).toList(),
                alertes.stream().map(TitreMapper::toDTO).toList()
        );
    }
}
