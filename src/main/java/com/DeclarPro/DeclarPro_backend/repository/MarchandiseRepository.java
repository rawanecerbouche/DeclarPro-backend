package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.Marchandise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarchandiseRepository extends JpaRepository<Marchandise, Long> {
    List<Marchandise> findByTitreImportation_Id(Long titreId);
}
