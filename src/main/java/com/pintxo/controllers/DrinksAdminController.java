package com.pintxo.controllers;

import com.pintxo.dtos.DrinkItemUpsertDTO;
import com.pintxo.models.DrinkCategory;
import com.pintxo.models.DrinkItem;
import com.pintxo.services.DrinkItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/drinks")
public class DrinksAdminController {

    private final DrinkItemService service;
    public DrinksAdminController(DrinkItemService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<DrinkItem>> list(@RequestParam(required = false) String category,
                                                @RequestParam(required = false) String q,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.list(category, q, page, size));
    }

    // ⬇️ NUEVO
    @GetMapping("/{id}")
    public ResponseEntity<DrinkItem> get(@PathVariable Long id) {
        return service.findOne(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DrinkItem> create(@RequestBody DrinkItemUpsertDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrinkItem> update(@PathVariable Long id, @RequestBody DrinkItemUpsertDTO dto) {
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

    @GetMapping("/categories-enum")
    public ResponseEntity<DrinkCategory[]> categoriesEnum() {
        return ResponseEntity.ok(DrinkCategory.values());
    }
}