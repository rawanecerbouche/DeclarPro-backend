package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.DocumentReferentiel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentReferentielRepository extends JpaRepository<DocumentReferentiel, Long> {
    List<DocumentReferentiel> findByActiveTrue();
}
