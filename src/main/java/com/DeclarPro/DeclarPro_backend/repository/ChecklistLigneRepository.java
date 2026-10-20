package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.ChecklistLigne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChecklistLigneRepository extends JpaRepository<ChecklistLigne, Long> {

    @Query("SELECT c FROM ChecklistLigne c " +
            "JOIN FETCH c.documentReferentiel d " +
            "JOIN FETCH d.organisme " +
            "WHERE c.titreImportation.id = :titreId")
    List<ChecklistLigne> findByTitreImportationIdWithDocument(Long titreId);

    Optional<ChecklistLigne> findByTitreImportation_IdAndDocumentReferentiel_IdAndRegleDocumentaire_Id(
            Long titreId, Long documentId, Long regleId);
}
