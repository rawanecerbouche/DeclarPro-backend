package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regle_documentaire")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "checklistLignes")
public class RegleDocumentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentReferentiel documentReferentiel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_juridique_id")
    private ReferenceJuridique referenceJuridique;

    @Column(name = "dimension", length = 50)
    private String dimension;

    @Column(name = "value", length = 255)
    private String value;

    @Column(name = "obligatoire")
    private Boolean obligatoire;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "regleDocumentaire", fetch = FetchType.LAZY)
    private List<ChecklistLigne> checklistLignes = new ArrayList<>();
}