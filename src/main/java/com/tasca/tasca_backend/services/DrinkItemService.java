package com.tasca.tasca_backend.services;

import com.tasca.tasca_backend.dtos.DrinkItemUpsertDTO;
import com.tasca.tasca_backend.dtos.DrinkPublicItem;
import com.tasca.tasca_backend.dtos.DrinksFullResponse;
import com.tasca.tasca_backend.models.DrinkCategory;
import com.tasca.tasca_backend.models.DrinkItem;
import com.tasca.tasca_backend.repositories.DrinkItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrinkItemService {
    private final DrinkItemRepository repo;

    public Page<DrinkItem> list(String category, String q, int page, int size) {
        DrinkCategory cat = null;
        if (category != null && !category.isBlank()) {
            cat = DrinkCategory.fromAny(category)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category: " + category));
        }
        return repo.search(cat, q, PageRequest.of(page, size));
    }

    public DrinkItem create(DrinkItemUpsertDTO dto) {
        return repo.save(apply(new DrinkItem(), dto));
    }

    public DrinkItem update(Long id, DrinkItemUpsertDTO dto) {
        DrinkItem d = repo.findById(id).orElseThrow();
        return repo.save(apply(d, dto));
    }

    public void delete(Long id) { repo.deleteById(id); }

    public void setAvailability(Long id, boolean value) {
        DrinkItem d = repo.findById(id).orElseThrow();
        d.setAvailable(value);
        repo.save(d);
    }

    public void setFeatured(Long id, boolean value) {
        DrinkItem d = repo.findById(id).orElseThrow();
        d.setFeatured(value);
        repo.save(d);
    }

    private DrinkItem apply(DrinkItem d, DrinkItemUpsertDTO dto) {
        DrinkCategory cat = DrinkCategory.fromAny(dto.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category: " + dto.getCategory()));
        d.setCategory(cat);
        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        d.setPrice(dto.getPrice());
        d.setDietaryTags(dto.getDietaryTags());
        d.setAllergens(dto.getAllergens());
        d.setAbv(dto.getAbv());
        d.setRegion(dto.getRegion());
        d.setGrape(dto.getGrape());
        if (dto.getAvailable() != null) d.setAvailable(dto.getAvailable());
        if (dto.getFeatured()  != null) d.setFeatured(dto.getFeatured());
        d.setImage(dto.getImage());
        d.setDisplayOrder(dto.getDisplayOrder());
        return d;
    }

    public DrinksFullResponse full() {
        // Agrupa disponibles por categoría y mapea a payload público
        List<DrinkItem> items = repo.findByAvailableTrueOrderByCategoryAscDisplayOrderAscIdAsc();
        Map<DrinkCategory, List<DrinkPublicItem>> grouped = items.stream()
                .collect(Collectors.groupingBy(DrinkItem::getCategory,
                        LinkedHashMap::new,
                        Collectors.mapping(this::toPublicItem, Collectors.toList())));

        List<DrinksFullResponse.CategoryBlock> cats = grouped.entrySet().stream()
                .map(e -> DrinksFullResponse.CategoryBlock.builder()
                        .name(e.getKey().getDisplayName())
                        .slug(e.getKey().getSlug())
                        .items(e.getValue())
                        .build())
                .toList();

        return DrinksFullResponse.builder()
                .currency("USD")
                .categories(cats)
                .build();
    }

    private DrinkPublicItem toPublicItem(DrinkItem d) {
        return DrinkPublicItem.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .price(d.getPrice())
                .available(d.isAvailable())
                .featured(d.isFeatured())
                .displayOrder(d.getDisplayOrder())
                .image(d.getImage())
                .abv(d.getAbv())
                .region(d.getRegion())
                .grape(d.getGrape())
                .dietaryTags(d.getDietaryTags())
                .allergens(d.getAllergens())
                .build();
    }

    public Optional<DrinkItem> findOne(Long id) {
        return repo.findById(id);
    }

}