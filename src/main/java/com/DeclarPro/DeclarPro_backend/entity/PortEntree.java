package com.DeclarPro.DeclarPro_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "port_entree")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "titres")
public class PortEntree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "ville", length = 100)
    private String ville;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "portEntree", fetch = FetchType.LAZY)
    private List<TitreImportation> titres = new ArrayList<>();
}