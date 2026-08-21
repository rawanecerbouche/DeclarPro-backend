package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "checklist_ligne")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"titreImportation", "documentReferentiel", "regleDocumentaire"})
public class ChecklistLigne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id", nullable = false)
    private TitreImportation titreImportation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentReferentiel documentReferentiel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regle_id")
    private RegleDocumentaire regleDocumentaire;

    @Column(name = "obligatoire")
    private Boolean obligatoire;

    @Column(name = "statut", length = 30)
    private String statut;

    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @Column(name = "date_creation")
    private LocalDate dateCreation;
}