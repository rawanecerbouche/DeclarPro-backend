package com.DeclarPro.DeclarPro_backend.controller;

import com.DeclarPro.DeclarPro_backend.dto.CalculFraisRequest;
import com.DeclarPro.DeclarPro_backend.dto.CalculFraisResponse;
import com.DeclarPro.DeclarPro_backend.service.CalculFraisService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CalculFraisController {

    private final CalculFraisService calculFraisService;

    public CalculFraisController(CalculFraisService calculFraisService) {
        this.calculFraisService = calculFraisService;
    }

    @PostMapping("/calcul-frais")
    public CalculFraisResponse calculer(@RequestBody CalculFraisRequest request) {
        return calculFraisService.calculer(request);
    }
}
