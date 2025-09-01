package com.tasca.tasca_backend.services;
import com.tasca.tasca_backend.dtos.MenuCategoryDTO;
import com.tasca.tasca_backend.dtos.MenuFullResponse;
import com.tasca.tasca_backend.dtos.MenuItemDTO;
import com.tasca.tasca_backend.models.MenuCategory;
import com.tasca.tasca_backend.models.MenuItem;
import com.tasca.tasca_backend.repositories.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuQueryService {

    private final MenuItemRepository itemRepo;

    @Transactional(readOnly = true)
    public MenuFullResponse getFull(String currency) {
        var items = itemRepo.findByAvailableTrueOrderByDisplayOrderAscIdAsc();

        Map<MenuCategory, List<MenuItem>> byCat = items.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory, LinkedHashMap::new, Collectors.toList()));

        var orderedCats = Arrays.stream(MenuCategory.values()).toList();

        List<MenuCategoryDTO> categoryDTOs = new ArrayList<>();
        for (MenuCategory cat : orderedCats) {
            var itemDTOs = byCat.getOrDefault(cat, List.of()).stream()
                    .map(this::toItemDTO)
                    .toList();
            categoryDTOs.add(new MenuCategoryDTO(cat.getDisplayName(), cat.getSlug(), itemDTOs));
        }

        return new MenuFullResponse(currency, categoryDTOs);
    }

    private MenuItemDTO toItemDTO(MenuItem i) {
        MenuItemDTO menuItemDTO = new MenuItemDTO(
                i.getId(),
                i.getName(),
                nvl(i.getDescription()),
                i.getPrice(),
                nvl(i.getDietaryTags()),
                nvl(i.getAllergens()),
                i.getSpicyLevel(),
                i.isHalal(),
                i.isAvailable(),
                i.isFeatured(),
                i.getImage(),
                i.getDisplayOrder() == null ? 999 : i.getDisplayOrder()
        );
        return menuItemDTO;
    }

    private static String nvl(String s) { return s == null ? "" : s; }
}
