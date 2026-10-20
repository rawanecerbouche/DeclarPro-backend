package com.DeclarPro.DeclarPro_backend.controller;

import com.DeclarPro.DeclarPro_backend.dto.TitreChecklistItemDTO;
import com.DeclarPro.DeclarPro_backend.dto.TitreCreateRequest;
import com.DeclarPro.DeclarPro_backend.dto.TitreRapportDTO;
import com.DeclarPro.DeclarPro_backend.dto.TitreResponse;
import com.DeclarPro.DeclarPro_backend.service.ChecklistService;
import com.DeclarPro.DeclarPro_backend.service.TitreImportationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/titres")
public class TitreImportationController {

    private final TitreImportationService service;
    private final ChecklistService checklistService;

    public TitreImportationController(TitreImportationService service, ChecklistService checklistService) {
        this.service = service;
        this.checklistService = checklistService;
    }

    @PostMapping
    public ResponseEntity<TitreResponse> create(@Valid @RequestBody TitreCreateRequest request) {
        log.info("Start calling creatte Titre ..... ");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTitre(request));
    }

    @GetMapping
    public List<TitreResponse> list(@RequestParam(required = false) String etat,
                                     @RequestParam(required = false) String numeroEnregistrement) {
        return service.listTitres(etat, numeroEnregistrement);
    }

    @GetMapping("/{id}")
    public TitreResponse get(@PathVariable Long id) {
        return service.getTitre(id);
    }

    @PutMapping("/{id}")
    public TitreResponse update(@PathVariable Long id, @Valid @RequestBody TitreCreateRequest request) {
        return service.updateTitre(id, request);
    }

    @GetMapping("/{id}/checklist")
    public List<TitreChecklistItemDTO> checklist(@PathVariable Long id) {
        return checklistService.getChecklistForTitre(id);
    }

    @PostMapping("/{id}/checklist/generate")
    public List<TitreChecklistItemDTO> generateChecklist(@PathVariable Long id) {
        return checklistService.generateAndPersistChecklistForTitre(id);
    }

    @GetMapping("/{id}/rapport")
    public TitreRapportDTO rapport(@PathVariable Long id) {
        return service.getRapport(id);
    }
}
