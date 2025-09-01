package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.dtos.MenuItemRequestDTO;
import com.tasca.tasca_backend.dtos.MenuItemResponseDTO;
import com.tasca.tasca_backend.dtos.MenuItemUpsertDTO;
import com.tasca.tasca_backend.models.MenuCategory;
import com.tasca.tasca_backend.models.MenuItem;
import com.tasca.tasca_backend.services.MenuItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/menu")
public class MenuAdminController {

    private final MenuItemService service;

    public MenuAdminController(MenuItemService service) { this.service = service; }

    // List para el dashboard con filtros/paginación
    @GetMapping
    public ResponseEntity<Page<MenuItem>> list(
            @RequestParam(required = false) String category, // ej: MEAT_TAPAS
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.list(category, q, page, size));
    }

    @PostMapping
    public ResponseEntity<MenuItem> create(@RequestBody MenuItemUpsertDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> update(@PathVariable Long id, @RequestBody MenuItemUpsertDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> setAvailability(@PathVariable Long id, @RequestParam boolean value) {
        service.setAvailability(id, value);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/featured")
    public ResponseEntity<Void> setFeatured(@PathVariable Long id, @RequestParam boolean value) {
        service.setFeatured(id, value);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/highlight")
    public ResponseEntity<Void> setMenuHighlight(@PathVariable Long id,
                                                 @RequestParam boolean value,
                                                 @RequestParam(required = false) String tag) {
        service.setMenuHighlight(id, value, tag);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories-enum")
    public ResponseEntity<MenuCategory[]> categoriesEnum() {
        // Jackson serializa los enums como strings por defecto: ["GREEN_TAPAS", "MEAT_TAPAS", ...]
        return ResponseEntity.ok(MenuCategory.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getById(@PathVariable Long id) {
        return service.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}