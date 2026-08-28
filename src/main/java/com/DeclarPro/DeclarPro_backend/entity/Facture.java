package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facture")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "titreImportation")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id", nullable = false)
    private TitreImportation titreImportation;

    @Column(name = "reference", length = 50)
    private String reference;

    @Column(name = "montant", precision = 14, scale = 2)
    private BigDecimal montant;

    @Column(name = "devise", length = 10)
    private String devise;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "fichier", length = 255)
    private String fichier;
}