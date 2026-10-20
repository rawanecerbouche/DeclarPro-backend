package com.DeclarPro.DeclarPro_backend.dto;

import java.time.LocalDate;

public record DocumentJointDTO(
        Long id,
        String type,
        String description,
        LocalDate dateDocument,
        String fichier
) {
}
