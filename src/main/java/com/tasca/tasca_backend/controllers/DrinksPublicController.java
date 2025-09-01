package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.dtos.DrinksFullResponse;
import com.tasca.tasca_backend.services.DrinkItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drinks")
public class DrinksPublicController {
    private final DrinkItemService service;
    public DrinksPublicController(DrinkItemService service) { this.service = service; }

    @GetMapping("/full")
    public ResponseEntity<DrinksFullResponse> full() {
        return ResponseEntity.ok(service.full());
    }
}
