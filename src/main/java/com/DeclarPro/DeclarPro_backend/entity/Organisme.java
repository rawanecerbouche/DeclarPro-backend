package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organisme")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "documentsReferentiels")
public class Organisme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "nom", length = 150)
    private String nom;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "organisme", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DocumentReferentiel> documentsReferentiels = new ArrayList<>();

    public void addDocumentReferentiel(DocumentReferentiel doc) {
        documentsReferentiels.add(doc);
        doc.setOrganisme(this);
    }

    public void removeDocumentReferentiel(DocumentReferentiel doc) {
        documentsReferentiels.remove(doc);
        doc.setOrganisme(null);
    }
}