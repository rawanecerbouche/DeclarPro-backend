package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "document_joint")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "titreImportation")
public class DocumentJoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id", nullable = false)
    private TitreImportation titreImportation;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "date_document")
    private LocalDate dateDocument;

    @Column(name = "fichier", length = 255)
    private String fichier;
}