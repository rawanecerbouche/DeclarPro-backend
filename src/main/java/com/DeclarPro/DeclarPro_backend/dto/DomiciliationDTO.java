package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;

public record DomiciliationDTO(
        Long id,
        BigDecimal montantGlobalFactures,
        BigDecimal montantFob,
        BigDecimal montantAssurance,
        BigDecimal montantFret,
        BigDecimal montantAccessoires,
        String rib,
        String codeBanque,
        String nomBanque,
        String codeGuichet,
        String nomGuichet
) {
}
