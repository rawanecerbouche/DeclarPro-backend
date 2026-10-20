package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.RegleDocumentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegleDocumentaireRepository extends JpaRepository<RegleDocumentaire, Long> {

    @Query("SELECT r FROM RegleDocumentaire r " +
            "JOIN FETCH r.documentReferentiel d " +
            "JOIN FETCH d.organisme " +
            "WHERE r.active = true AND d.active = true")
    List<RegleDocumentaire> findActiveWithDocumentAndOrganisme();
}
