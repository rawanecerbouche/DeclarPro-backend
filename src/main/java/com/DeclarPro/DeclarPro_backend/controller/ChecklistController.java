package com.DeclarPro.DeclarPro_backend.controller;

import com.DeclarPro.DeclarPro_backend.dto.ChecklistItemDTO;
import com.DeclarPro.DeclarPro_backend.service.ChecklistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChecklistController {

    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @GetMapping("/checklist")
    public List<ChecklistItemDTO> checklist(@RequestParam String codeSh,
                                             @RequestParam String categorieProduit,
                                             @RequestParam String regimeDouanier,
                                             @RequestParam String paysOrigine) {
        return checklistService.generateChecklist(codeSh, categorieProduit, regimeDouanier, paysOrigine);
    }
}
