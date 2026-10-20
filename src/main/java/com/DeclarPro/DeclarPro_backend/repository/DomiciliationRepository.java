package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.Domiciliation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomiciliationRepository extends JpaRepository<Domiciliation, Long> {
    Optional<Domiciliation> findByTitreImportation_Id(Long titreId);
}
