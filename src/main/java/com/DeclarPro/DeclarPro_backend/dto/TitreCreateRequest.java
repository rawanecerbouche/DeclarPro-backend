package com.DeclarPro.DeclarPro_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TitreCreateRequest(
        @NotNull ImportateurDTO importateur,
        String numeroEnregistrement,
        String categorieTitre,
        @NotBlank String expediteur,
        @NotBlank String devise,
        String modalitePaiement,
        String regimeDouanier,
        String bureauDouanier,
        @NotBlank String incoterm,
        Long portEntreeId,
        String observations,
        DomiciliationDTO domiciliation,
        List<MarchandiseDTO> marchandises,
        List<FactureDTO> factures,
        List<DocumentJointDTO> documents
) {
}
