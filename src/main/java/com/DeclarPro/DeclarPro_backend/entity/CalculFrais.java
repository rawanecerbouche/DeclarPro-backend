package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Persisted result of a Service 5 (calcul des droits/TVA/frais) run, when it was executed
 * against a specific titre_importation rather than as a standalone quick-calc.
 */
@Entity
@Table(name = "calcul_frais")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "titreImportation")
public class CalculFrais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id", nullable = false)
    private TitreImportation titreImportation;

    @Column(name = "valeur_cif", precision = 14, scale = 2)
    private BigDecimal valeurCif;

    @Column(name = "droit_douane_taux", precision = 6, scale = 2)
    private BigDecimal droitDouaneTaux;

    @Column(name = "montant_dd", precision = 14, scale = 2)
    private BigDecimal montantDD;

    @Column(name = "tpi_taux", precision = 6, scale = 2)
    private BigDecimal tpiTaux;

    @Column(name = "montant_tpi", precision = 14, scale = 2)
    private BigDecimal montantTPI;

    @Column(name = "tva_taux", precision = 6, scale = 2)
    private BigDecimal tvaTaux;

    @Column(name = "base_tva", precision = 14, scale = 2)
    private BigDecimal baseTVA;

    @Column(name = "montant_tva", precision = 14, scale = 2)
    private BigDecimal montantTVA;

    @Column(name = "tic_taux", precision = 6, scale = 2)
    private BigDecimal ticTaux;

    @Column(name = "montant_tic", precision = 14, scale = 2)
    private BigDecimal montantTIC;

    @Column(name = "bmda_taux", precision = 6, scale = 2)
    private BigDecimal bmdaTaux;

    @Column(name = "montant_bmda", precision = 14, scale = 2)
    private BigDecimal montantBMDA;

    @Column(name = "total_estime", precision = 14, scale = 2)
    private BigDecimal totalEstime;

    @Column(name = "date_creation")
    private LocalDate dateCreation;
}
