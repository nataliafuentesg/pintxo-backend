package com.pintxo.controllers;
import com.pintxo.models.MenuHighlightDay;
import com.pintxo.models.MenuItem;
import com.pintxo.repositories.MenuHighlightDayRepository;
import com.pintxo.repositories.MenuItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/highlights")
public class HighlightPublicController {

    private final MenuHighlightDayRepository dayRepo;
    private final MenuItemRepository itemRepo;

    public HighlightPublicController(MenuHighlightDayRepository dayRepo, MenuItemRepository itemRepo) {
        this.dayRepo = dayRepo; this.itemRepo = itemRepo;
    }

    @GetMapping
    public ResponseEntity<List<HighlightCardDTO>> get(@RequestParam(required = false) String date) {
        LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);

        // 1) Programados y activos para esa fecha
        var scheduled = dayRepo.findByDateAndActiveTrueOrderByPositionAsc(d);
        if (!scheduled.isEmpty()) {
            return ResponseEntity.ok(
                    scheduled.stream().map(HighlightCardDTO::fromDay).toList()
            );
        }

        // 2) Fallback a items marcados como highlight y disponibles
        var sort = org.springframework.data.domain.Sort
                .by("displayOrder").ascending().and(org.springframework.data.domain.Sort.by("id").ascending());
        var fallback = itemRepo.findByMenuHighlightTrueAndAvailableTrue(sort);

        return ResponseEntity.ok(
                fallback.stream().map(HighlightCardDTO::fromItem).toList()
        );
    }

    // DTO mínimo para el carrusel público (array estable)
    public static class HighlightCardDTO {
        public Long id;              // id del MenuItem
        public String name;
        public String description;
        public double price;
        public String image;
        public String tag;           // tagOverride o menuHighlightTag
        public Integer position;     // solo si viene de programación
        public String category;

        static HighlightCardDTO fromDay(MenuHighlightDay m) {
            var it = m.getItem();
            var dto = new HighlightCardDTO();
            dto.id = it.getId();
            dto.name = it.getName();
            dto.description = it.getDescription();
            dto.price = it.getPrice();
            dto.image = it.getImage();
            dto.tag = (m.getTagOverride() != null && !m.getTagOverride().isBlank())
                    ? m.getTagOverride()
                    : it.getMenuHighlightTag();
            dto.position = m.getPosition();
            dto.category = it.getCategory().name();
            return dto;
        }

        static HighlightCardDTO fromItem(MenuItem it) {
            var dto = new HighlightCardDTO();
            dto.id = it.getId();
            dto.name = it.getName();
            dto.description = it.getDescription();
            dto.price = it.getPrice();
            dto.image = it.getImage();
            dto.tag = it.getMenuHighlightTag();
            dto.position = null;
            dto.category = it.getCategory().name();
            return dto;
        }
    }
}