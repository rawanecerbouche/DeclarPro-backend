package com.DeclarPro.DeclarPro_backend.repository;

import com.DeclarPro.DeclarPro_backend.entity.RegleCoherence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegleCoherenceRepository extends JpaRepository<RegleCoherence, Long> {
    List<RegleCoherence> findByActiveTrue();
}
