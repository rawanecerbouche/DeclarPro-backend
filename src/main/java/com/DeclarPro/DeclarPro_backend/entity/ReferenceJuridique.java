package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reference_juridique")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "reglesDocumentaires")
public class ReferenceJuridique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "type_texte", length = 50)
    private String typeTexte;

    @Column(name = "numero", length = 50)
    private String numero;

    @Column(name = "titre", length = 255)
    private String titre;

    @Column(name = "date_publication")
    private LocalDate datePublication;

    @Column(name = "date_entree_vigueur")
    private LocalDate dateEntreeVigueur;

    @Column(name = "date_fin_validite")
    private LocalDate dateFinValidite;

    @Column(name = "source", length = 150)
    private String source;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "referenceJuridique", fetch = FetchType.LAZY)
    private List<RegleDocumentaire> reglesDocumentaires = new ArrayList<>();
}
