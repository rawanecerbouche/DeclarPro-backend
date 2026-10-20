package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.TitreImportation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TitreImportationRepository extends JpaRepository<TitreImportation, Long> {
    List<TitreImportation> findByEtat(String etat);
    List<TitreImportation> findByNumeroEnregistrementContainingIgnoreCase(String numeroEnregistrement);
}
