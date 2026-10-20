package com.DeclarPro.DeclarPro_backend.dto;

public record ImportateurDTO(
        Long id,
        String description,
        String centreRc,
        String ifu,
        String numeroEnregistrement,
        String numeroRc,
        String codePin,
        String taxeProfessionnelle,
        String fichierDce,
        String telephoneMobile,
        String telephoneFixe,
        String email,
        String adresse,
        String voie,
        String codePostal,
        String localite
) {
}
