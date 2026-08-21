package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "document_referentiel")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"reglesDocumentaires", "checklistLignes"})
public class DocumentReferentiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisme_id", nullable = false)
    private Organisme organisme;

    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "libelle", length = 150)
    private String libelle;

    @Column(name = "type_portnet", length = 50)
    private String typePortnet;

    @Column(name = "delai_jours")
    private Integer delaiJours;

    @Column(name = "critique")
    private Boolean critique;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "documentReferentiel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RegleDocumentaire> reglesDocumentaires = new ArrayList<>();

    @OneToMany(mappedBy = "documentReferentiel", fetch = FetchType.LAZY)
    private List<ChecklistLigne> checklistLignes = new ArrayList<>();

    public void addRegleDocumentaire(RegleDocumentaire regle) {
        reglesDocumentaires.add(regle);
        regle.setDocumentReferentiel(this);
    }

    public void removeRegleDocumentaire(RegleDocumentaire regle) {
        reglesDocumentaires.remove(regle);
        regle.setDocumentReferentiel(null);
    }
}