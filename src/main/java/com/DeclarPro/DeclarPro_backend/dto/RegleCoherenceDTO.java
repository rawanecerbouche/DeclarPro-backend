package com.DeclarPro.DeclarPro_backend.dto;

public record RegleCoherenceDTO(
        Long id,
        String code,
        String libelle,
        String dimension,
        String operateur,
        String valeurReference,
        String severite,
        Integer scorePenalite,
        String message
) {
}
