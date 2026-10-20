package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Full dossier report for a titre_importation (Service 9 - Export PDF de synthese) : titre info,
 * importateur, domiciliation, marchandises, factures, documents joints, coherence alerts, and any
 * persisted calcul-frais (Service 5) results for that titre.
 */
public record TitreRapportDTO(
        Long id,
        String numeroEnregistrement,
        String categorieTitre,
        String etat,
        String expediteur,
        String devise,
        String modalitePaiement,
        String regimeDouanier,
        String bureauDouanier,
        String incoterm,
        PortEntreeRefDTO portEntree,
        String observations,
        LocalDate dateCreation,
        BigDecimal valeurEnDouane,
        ImportateurDTO importateur,
        DomiciliationDTO domiciliation,
        List<MarchandiseDTO> marchandises,
        List<FactureDTO> factures,
        List<DocumentJointDTO> documents,
        List<AlerteDTO> alertes,
        List<CalculFraisDTO> calculsFrais
) {
    public record PortEntreeRefDTO(Long id, String code, String nom, String ville) {
    }
}
