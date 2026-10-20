package com.DeclarPro.DeclarPro_backend.dto;

import java.time.LocalDate;

public record AlerteDTO(
        Long id,
        String regleCode,
        String severite,
        String message,
        String dimension,
        String valeurDetectee,
        String valeurAttendue,
        Integer scorePenalite,
        LocalDate dateCreation
) {
}
