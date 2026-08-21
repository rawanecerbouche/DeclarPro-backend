package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regle_coherence")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "alertes")
public class RegleCoherence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "libelle", length = 150)
    private String libelle;

    @Column(name = "dimension", length = 50)
    private String dimension;

    @Column(name = "operateur", length = 20)
    private String operateur;

    @Column(name = "valeur_reference", length = 100)
    private String valeurReference;

    @Column(name = "severite", length = 20)
    private String severite;

    @Column(name = "score_penalite")
    private Integer scorePenalite;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "regleCoherence", fetch = FetchType.LAZY)
    private List<Alerte> alertes = new ArrayList<>();
}