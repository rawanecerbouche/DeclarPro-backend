package com.DeclarPro.DeclarPro_backend.dto;

public record ChecklistItemDTO(
        Long documentId,
        String code,
        String libelle,
        String typePortnet,
        OrganismeRefDTO organisme,
        Integer delaiJours,
        Boolean critique,
        String motif
) {
    public record OrganismeRefDTO(String code, String nom) {
    }
}
