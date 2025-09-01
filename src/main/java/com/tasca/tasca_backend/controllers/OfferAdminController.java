package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.dtos.OfferUpsertDTO;
import com.tasca.tasca_backend.models.OfferBanner;
import com.tasca.tasca_backend.services.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/offers")
public class OfferAdminController {

    private final OfferService service;

    @GetMapping
    public ResponseEntity<Page<OfferBanner>> list(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(service.listAdmin(q, page, size));
    }

    @PostMapping
    public ResponseEntity<OfferBanner> create(@RequestBody OfferUpsertDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferBanner> update(@PathVariable Long id, @RequestBody OfferUpsertDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean value) {
        service.setActive(id, value);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferBanner> get(@PathVariable Long id) {
        return ResponseEntity.of(service.findById(id));
    }
}
