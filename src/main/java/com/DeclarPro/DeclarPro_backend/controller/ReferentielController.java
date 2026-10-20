package com.DeclarPro.DeclarPro_backend.controller;

import com.DeclarPro.DeclarPro_backend.dto.RegleCoherenceDTO;
import com.DeclarPro.DeclarPro_backend.dto.TitreMapper;
import com.DeclarPro.DeclarPro_backend.repository.RegleCoherenceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/referentiels")
public class ReferentielController {

    private final RegleCoherenceRepository regleCoherenceRepository;

    public ReferentielController(RegleCoherenceRepository regleCoherenceRepository) {
        this.regleCoherenceRepository = regleCoherenceRepository;
    }

    @GetMapping("/regles")
    public List<RegleCoherenceDTO> regles() {
        return regleCoherenceRepository.findByActiveTrue().stream().map(TitreMapper::toDTO).toList();
    }
}
