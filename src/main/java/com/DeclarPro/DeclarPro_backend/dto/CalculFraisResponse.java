package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CalculFraisResponse(
        Long id,
        Long titreId,
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
