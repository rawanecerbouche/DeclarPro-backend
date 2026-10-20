package com.DeclarPro.DeclarPro_backend.dto;

public record TitreChecklistItemDTO(
        Long ligneId,
        Long documentId,
        String code,
        String libelle,
        String typePortnet,
        ChecklistItemDTO.OrganismeRefDTO organisme,
        Integer delaiJours,
        Boolean critique,
        Boolean obligatoire,
        String statut,
        String motif
) {
}
