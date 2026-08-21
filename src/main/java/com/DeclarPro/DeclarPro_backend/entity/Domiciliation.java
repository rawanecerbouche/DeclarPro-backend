package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "domiciliation")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "titreImportation")
public class Domiciliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id", nullable = false, unique = true)
    private TitreImportation titreImportation;

    @Column(name = "montant_global_factures", precision = 14, scale = 2)
    private BigDecimal montantGlobalFactures;

    @Column(name = "montant_fob", precision = 14, scale = 2)
    private BigDecimal montantFob;

    @Column(name = "montant_assurance", precision = 14, scale = 2)
    private BigDecimal montantAssurance;

    @Column(name = "montant_fret", precision = 14, scale = 2)
    private BigDecimal montantFret;

    @Column(name = "montant_accessoires", precision = 14, scale = 2)
    private BigDecimal montantAccessoires;

    @Column(name = "rib", length = 34)
    private String rib;

    @Column(name = "code_banque", length = 20)
    private String codeBanque;

    @Column(name = "nom_banque", length = 100)
    private String nomBanque;

    @Column(name = "code_guichet", length = 20)
    private String codeGuichet;

    @Column(name = "nom_guichet", length = 100)
    private String nomGuichet;
}