package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.dtos.MenuFullResponse;
import com.tasca.tasca_backend.services.MenuQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuQueryService menuService;

    @Value("${tasca.menu.currency:USD}")
    private String currency;

    @GetMapping("/full")
    public MenuFullResponse full() {
        // Devuelve el JSON grande: currency + categories + items (dietary_tags, spicy_level, order, etc.)
        return menuService.getFull(currency);
    }
}