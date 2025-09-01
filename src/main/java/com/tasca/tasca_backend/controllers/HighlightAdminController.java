package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.models.MenuHighlightDay;
import com.tasca.tasca_backend.services.HighlightDayService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/highlights")
public class HighlightAdminController {

    private final HighlightDayService service;

    public HighlightAdminController(HighlightDayService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<MenuHighlightDay>> list(
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        LocalDate d = (date == null || date.isBlank()) ? null : LocalDate.parse(date);
        return ResponseEntity.ok(service.list(d, page, size));
    }

    // Upsert por query params
    @PostMapping
    public ResponseEntity<MenuHighlightDay> upsert(
            @RequestParam String date,
            @RequestParam Long itemId,
            @RequestParam(required=false) Integer position,
            @RequestParam(required=false) String tagOverride,
            @RequestParam(required=false, defaultValue = "true") boolean active
    ) {
        return ResponseEntity.ok(
                service.upsert(LocalDate.parse(date), itemId, position, tagOverride, active)
        );
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean value) {
        service.setActive(id, value);
        return ResponseEntity.noContent().build();
    }

    // Reordenar con CSV: positions=5:1,7:2,9:3
    @PostMapping("/reorder")
    public ResponseEntity<Void> reorder(@RequestParam String date, @RequestParam String positions) {
        service.reorder(LocalDate.parse(date), positions);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}