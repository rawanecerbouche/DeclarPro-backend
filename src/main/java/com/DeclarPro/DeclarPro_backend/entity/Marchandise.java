package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "marchandise")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "titreImportation")
public class Marchandise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id", nullable = false)
    private TitreImportation titreImportation;

    @Column(name = "designation", length = 255)
    private String designation;

    @Column(name = "code_sh", length = 20)
    private String codeSh;

    @Column(name = "poids_net", precision = 12, scale = 2)
    private BigDecimal poidsNet;

    @Column(name = "poids_brut", precision = 12, scale = 2)
    private BigDecimal poidsBrut;

    @Column(name = "pays_origine", length = 5)
    private String paysOrigine;

    @Column(name = "categorie_produit", length = 30)
    private String categorieProduit;

    @Column(name = "unite", length = 20)
    private String unite;

    @Column(name = "quantite", precision = 12, scale = 2)
    private BigDecimal quantite;

    @Column(name = "unite_comp_liberele", length = 20)
    private String uniteCompLiberele;
}