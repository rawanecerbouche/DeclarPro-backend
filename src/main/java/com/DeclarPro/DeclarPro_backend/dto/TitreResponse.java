package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TitreResponse(
        Long id,
        Long importateurId,
        String numeroEnregistrement,
        String categorieTitre,
        String etat,
        String expediteur,
        String devise,
        String modalitePaiement,
        String regimeDouanier,
        String bureauDouanier,
        String incoterm,
        Long portEntreeId,
        String observations,
        LocalDate dateCreation,
        BigDecimal valeurEnDouane,
        DomiciliationDTO domiciliation,
        List<MarchandiseDTO> marchandises,
        List<FactureDTO> factures,
        List<DocumentJointDTO> documents,
        List<AlerteDTO> alertes
) {
}
