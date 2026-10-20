package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;

/**
 * Service 5 - Calcul des droits, TVA et frais annexes.
 * valeurCif: if provided, used directly; otherwise computed as montantFob + montantFret + montantAssurance.
 * droitDouaneTaux / tvaTaux / ticTaux / bmdaTaux are percentages (e.g. 10 for 10%), chosen/entered by the
 * user rather than looked up from the HS code — no official tariff database is integrated in this project.
 * titreId: optional. When provided, the result is persisted (CalculFrais) and linked to that titre so
 * it can be surfaced later in the Service 9 report; when absent, this stays a standalone quick-calc.
 */
public record CalculFraisRequest(
        Long titreId,
        BigDecimal valeurCif,
        BigDecimal montantFob,
        BigDecimal montantFret,
        BigDecimal montantAssurance,
        BigDecimal droitDouaneTaux,
        BigDecimal tvaTaux,
        BigDecimal ticTaux,
        BigDecimal bmdaTaux
) {
}
