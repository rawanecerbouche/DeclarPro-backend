package com.DeclarPro.DeclarPro_backend.dto;

import java.math.BigDecimal;

public record MarchandiseDTO(
        Long id,
        String designation,
        String codeSh,
        BigDecimal poidsNet,
        BigDecimal poidsBrut,
        String paysOrigine,
        String categorieProduit,
        String unite,
        BigDecimal quantite,
        String uniteCompLiberele
) {
}
