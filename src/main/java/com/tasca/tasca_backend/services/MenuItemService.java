package com.tasca.tasca_backend.services;

import com.tasca.tasca_backend.dtos.MenuItemResponseDTO;
import com.tasca.tasca_backend.dtos.MenuItemRequestDTO;
import com.tasca.tasca_backend.dtos.MenuItemUpsertDTO;
import com.tasca.tasca_backend.models.MenuCategory;
import com.tasca.tasca_backend.models.MenuItem;
import com.tasca.tasca_backend.repositories.MenuItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MenuItemService {

    private final MenuItemRepository repo;

    public MenuItemService(MenuItemRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public Page<MenuItem> list(String category, String q, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page,0), Math.max(size,1),
                Sort.by("displayOrder").ascending().and(Sort.by("id").descending()));

        boolean hasQ = q != null && !q.isBlank();
        boolean hasCat = category != null && !category.isBlank();

        if (hasCat && hasQ) {
            return repo.findByCategoryAndNameContainingIgnoreCase(parseCategory(category), q.trim(), pageable);
        } else if (hasCat) {
            return repo.findByCategory(parseCategory(category), pageable);
        } else if (hasQ) {
            return repo.findByNameContainingIgnoreCase(q.trim(), pageable);
        } else {
            return repo.findAll(pageable);
        }
    }

    public MenuItem create(MenuItemUpsertDTO dto) {
        validate(dto);
        MenuItem it = new MenuItem();
        apply(dto, it);
        return repo.save(it);
    }

    public MenuItem update(Long id, MenuItemUpsertDTO dto) {
        validate(dto);
        MenuItem it = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
        apply(dto, it);
        return repo.save(it);
    }

    public void delete(Long id) {
        if (repo.existsById(id)) repo.deleteById(id);
    }

    public void setAvailability(Long id, boolean value) {
        MenuItem it = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
        it.setAvailable(value);
        repo.save(it);
    }

    public void setFeatured(Long id, boolean value) {
        MenuItem it = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
        it.setFeatured(value);
        repo.save(it);
    }

    public void setMenuHighlight(Long id, boolean value, String tag) {
        MenuItem it = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
        it.setMenuHighlight(value);
        if (tag != null) it.setMenuHighlightTag(tag);
        repo.save(it);
    }

    /* helpers */
    private void validate(MenuItemUpsertDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Payload is required");
        if (dto.getCategory() == null) throw new IllegalArgumentException("category is required");
        if (dto.getName() == null || dto.getName().trim().isEmpty()) throw new IllegalArgumentException("name is required");
        if (dto.getPrice() < 0) throw new IllegalArgumentException("price must be >= 0");
        if (dto.getSpicyLevel() < 0 || dto.getSpicyLevel() > 3) throw new IllegalArgumentException("spicyLevel must be 0..3");
    }

    private void apply(MenuItemUpsertDTO dto, MenuItem it) {
        it.setCategory(dto.getCategory());
        it.setName(dto.getName().trim());
        it.setDescription(dto.getDescription());
        it.setPrice(dto.getPrice());
        it.setDietaryTags(dto.getDietaryTags());
        it.setAllergens(dto.getAllergens());
        it.setSpicyLevel(dto.getSpicyLevel());
        it.setHalal(dto.isHalal());
        if (dto.getAvailable() != null) it.setAvailable(dto.getAvailable());
        if (dto.getFeatured() != null) it.setFeatured(dto.getFeatured());
        if (dto.getMenuHighlight() != null) it.setMenuHighlight(dto.getMenuHighlight());
        it.setMenuHighlightTag(dto.getMenuHighlightTag());
        it.setImage(dto.getImage());
        it.setDisplayOrder(dto.getDisplayOrder());
    }

    private MenuCategory parseCategory(String raw) {
        // Acepta "MEAT_TAPAS" (enum) o "meat-tapas" (slug) si quieres extender
        try { return Enum.valueOf(MenuCategory.class, raw); }
        catch (IllegalArgumentException e) {
            // opcional: mapear por slug/displayName si lo necesitas
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Optional<MenuItem> findOne(Long id) {
        return repo.findById(id);
    }
}