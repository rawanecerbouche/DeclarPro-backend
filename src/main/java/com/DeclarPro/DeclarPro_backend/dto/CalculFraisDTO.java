package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Persisted calcul-frais result, used in the S9 titre report. */
public record CalculFraisDTO(
        Long id,
        BigDecimal valeurCif,
        BigDecimal droitDouaneTaux,
        BigDecimal montantDD,
        BigDecimal tpiTaux,
        BigDecimal montantTPI,
        BigDecimal tvaTaux,
        BigDecimal baseTVA,
        BigDecimal montantTVA,
        BigDecimal ticTaux,
        BigDecimal montantTIC,
        BigDecimal bmdaTaux,
        BigDecimal montantBMDA,
        BigDecimal totalEstime,
        LocalDate dateCreation
) {
}
