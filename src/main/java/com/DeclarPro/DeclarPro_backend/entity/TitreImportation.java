package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titre_importation")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"marchandises", "factures", "documentsJoints", "checklistLignes", "alertes", "domiciliation"})
public class TitreImportation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importateur_id", nullable = false)
    private Importateur importateur;

    @NotBlank
    @Column(name = "numero_enregistrement", length = 50)
    private String numeroEnregistrement;

    @Column(name = "etat", length = 30)
    private String etat;

    @Column(name = "expediteur", length = 150)
    private String expediteur;

    @Column(name = "devise", length = 10)
    private String devise;

    @Column(name = "modalite_paiement", length = 50)
    private String modalitePaiement;

    @Column(name = "regime_douanier", length = 50)
    private String regimeDouanier;

    @Column(name = "bureau_douanier", length = 50)
    private String bureauDouanier;

    @Column(name = "incoterm", length = 20)
    private String incoterm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_entree_id")
    private PortEntree portEntree;

    @Column(name = "observations", length = 500)
    private String observations;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @OneToMany(mappedBy = "titreImportation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Marchandise> marchandises = new ArrayList<>();

    @OneToMany(mappedBy = "titreImportation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Facture> factures = new ArrayList<>();

    @OneToOne(mappedBy = "titreImportation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Domiciliation domiciliation;

    @OneToMany(mappedBy = "titreImportation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DocumentJoint> documentsJoints = new ArrayList<>();

    @OneToMany(mappedBy = "titreImportation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChecklistLigne> checklistLignes = new ArrayList<>();

    @OneToMany(mappedBy = "titreImportation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Alerte> alertes = new ArrayList<>();

    // --- helper methods ---

    public void addMarchandise(Marchandise m) {
        marchandises.add(m);
        m.setTitreImportation(this);
    }

    public void removeMarchandise(Marchandise m) {
        marchandises.remove(m);
        m.setTitreImportation(null);
    }

    public void addFacture(Facture f) {
        factures.add(f);
        f.setTitreImportation(this);
    }

    public void removeFacture(Facture f) {
        factures.remove(f);
        f.setTitreImportation(null);
    }

    public void addDocumentJoint(DocumentJoint dj) {
        documentsJoints.add(dj);
        dj.setTitreImportation(this);
    }

    public void removeDocumentJoint(DocumentJoint dj) {
        documentsJoints.remove(dj);
        dj.setTitreImportation(null);
    }

    public void setDomiciliation(Domiciliation domiciliation) {
        if (domiciliation == null) {
            if (this.domiciliation != null) {
                this.domiciliation.setTitreImportation(null);
            }
        } else {
            domiciliation.setTitreImportation(this);
        }
        this.domiciliation = domiciliation;
    }
}