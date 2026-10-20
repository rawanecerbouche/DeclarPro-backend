package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.DocumentJoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentJointRepository extends JpaRepository<DocumentJoint, Long> {
    List<DocumentJoint> findByTitreImportation_Id(Long titreId);
}
