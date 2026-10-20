package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.Importateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImportateurRepository extends JpaRepository<Importateur, Long> {
    Optional<Importateur> findByIfu(String ifu);
}
