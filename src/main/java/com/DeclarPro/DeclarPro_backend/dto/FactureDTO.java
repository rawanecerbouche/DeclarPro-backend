package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FactureDTO(
        Long id,
        String reference,
        BigDecimal montant,
        String devise,
        LocalDate date,
        String fichier
) {
}
