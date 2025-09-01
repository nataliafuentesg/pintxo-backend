package com.tasca.tasca_backend.services;

import com.tasca.tasca_backend.models.MenuHighlightDay;
import com.tasca.tasca_backend.models.MenuItem;
import com.tasca.tasca_backend.repositories.MenuHighlightDayRepository;
import com.tasca.tasca_backend.repositories.MenuItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class HighlightDayService {

    private final MenuHighlightDayRepository repo;
    private final MenuItemRepository itemRepo;

    public HighlightDayService(MenuHighlightDayRepository repo, MenuItemRepository itemRepo) {
        this.repo = repo;
        this.itemRepo = itemRepo;
    }

    /* ADMIN */
    @Transactional(readOnly = true)
    public Page<MenuHighlightDay> list(LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page,0), Math.max(size,1),
                Sort.by("position").ascending().and(Sort.by("id").ascending()));
        return (date == null) ? repo.findAll(pageable) : repo.findByDate(date, pageable);
    }

    public MenuHighlightDay upsert(LocalDate date, Long itemId, Integer position, String tagOverride, boolean active) {
        if (date == null) throw new IllegalArgumentException("date is required");
        if (itemId == null) throw new IllegalArgumentException("itemId is required");

        MenuItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

        MenuHighlightDay m = repo.findByDateAndItem_Id(date, itemId).orElse(null);
        if (m == null) {
            m = new MenuHighlightDay();
            m.setDate(date);
            m.setItem(item);
        }
        if (position != null) m.setPosition(position);
        m.setTagOverride(tagOverride);
        m.setActive(active);
        return repo.save(m);
    }

    public void setActive(Long id, boolean value) {
        MenuHighlightDay m = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Highlight not found: " + id));
        m.setActive(value);
        repo.save(m);
    }

    public void reorder(LocalDate date, String positionsCsv) {
        if (date == null) throw new IllegalArgumentException("date is required");
        if (positionsCsv == null || positionsCsv.isBlank()) return;

        Map<Long,Integer> map = new HashMap<>();
        for (String pair : positionsCsv.split(",")) {
            String[] p = pair.split(":");
            if (p.length == 2) {
                Long id = Long.valueOf(p[0].trim());
                Integer pos = Integer.valueOf(p[1].trim());
                map.put(id, pos);
            }
        }
        List<MenuHighlightDay> list = repo.findByDateOrderByPositionAsc(date);
        for (MenuHighlightDay m : list) {
            Integer pos = map.get(m.getId());
            if (pos != null) m.setPosition(pos);
        }
        repo.saveAll(list);
    }

    public void delete(Long id) {
        if (repo.existsById(id)) repo.deleteById(id);
    }

    /* PÚBLICO (si aún no lo tienes, aquí está el fallback) */
    @Transactional(readOnly = true)
    public List<MenuItem> fallbackHighlights(LocalDate date) {
        // Si no hay programados activos ese día, usar los menuHighlight=true & available=true
        var sort = Sort.by("displayOrder").ascending().and(Sort.by("id").ascending());
        return itemRepo.findByMenuHighlightTrueAndAvailableTrue(sort);
    }
}