package com.DeclarPro.DeclarPro_backend.entity;
import com.DeclarPro.DeclarPro_backend.entity.TitreImportation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.valueextraction.UnwrapByDefault;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.util.Lazy;
import org.hibernate.action.internal.OrphanRemovalAction;
import org.hibernate.sql.results.graph.Fetch;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table( name="Importateur",
        uniqueConstraints = {
           @UniqueConstraint(name = "UK_importateur_ifu",columnNames = "ifu"),
           @UniqueConstraint (name = "UK_importateur_centre_rc",columnNames ="centre_rc")
        })
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude ="titres")
public class Importateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID_importateur;
    @NotBlank
    @Column(name = "Description",length = 255,nullable = false)
    private String Desciption;

    /** Centre du registre de commerce (front : centreRC). */
    @Column(name = "centre_rc", length = 100)
    private String centreRc;

    /** Identifiant Fiscal Unique (front : ifu). */
    @Column(name = "ifu", length = 30)
    private String ifu;


    /** Numero d'enregistrement au registre de commerce (front : regNum). */
    @Column(name = "numero_enregistrement", length = 50)
    private String numeroEnregistrement;

    /** N° RC (front : numRC). */
    @Column(name = "numero_rc", length = 50)
    private String numeroRc;

    /** Code PIN PortNet (front : codePin). */
    @Column(name = "code_pin", length = 30)
    private String codePin;

    /** Taxe professionnelle (front : taxProf). */
    @Column(name = "taxe_professionnelle", length = 50)
    private String taxeProfessionnelle;

    /** Chemin / nom du fichier DCE televerse (front : fileDce). */
    @Column(name = "fichier_dce", length = 255)
    private String fichierDce;

    @Column(name = "telephone_mobile", length = 20)
    private String telephoneMobile;

    @Column(name = "telephone_fixe", length = 20)
    private String telephoneFixe;

    @Email
    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @Column(name = "voie", length = 150)
    private String voie;

    @Column(name = "code_postal", length = 10)
    private String codePostal;

    @Column(name = "localite", length = 100)
    private String localite;

    @OneToMany(mappedBy = "importateur", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TitreImportation> titres = new ArrayList<>();

    public void addTitre(TitreImportation titre){
        titres.add(titre);
        titre.setImportateur(this);//red cause i havent created les attributs de titreImporateur yett
    }
    public void removeTitre(TitreImportation titre){
        titres.remove(titre);
        titre.setImportateur(null);
    }


}
