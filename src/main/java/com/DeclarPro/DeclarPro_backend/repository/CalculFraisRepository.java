package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.CalculFrais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalculFraisRepository extends JpaRepository<CalculFrais, Long> {
    List<CalculFrais> findByTitreImportation_IdOrderByDateCreationDescIdDesc(Long titreId);
}
