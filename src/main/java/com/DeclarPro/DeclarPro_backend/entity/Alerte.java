package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "alerte")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"titreImportation", "regleCoherence"})
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id", nullable = false)
    private TitreImportation titreImportation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regle_id")
    private RegleCoherence regleCoherence;

    @Column(name = "severite", length = 20)
    private String severite;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "dimension", length = 50)
    private String dimension;

    @Column(name = "valeur_detectee", length = 100)
    private String valeurDetectee;

    @Column(name = "valeur_attendue", length = 100)
    private String valeurAttendue;

    @Column(name = "score_penalite")
    private Integer scorePenalite;

    @Column(name = "date_creation")
    private LocalDate dateCreation;
}