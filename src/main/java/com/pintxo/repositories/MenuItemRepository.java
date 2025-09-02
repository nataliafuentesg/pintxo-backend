package com.pintxo.repositories;

import com.pintxo.models.MenuCategory;
import com.pintxo.models.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByAvailableTrueOrderByDisplayOrderAscIdAsc();
   List<MenuItem> findByCategoryAndAvailableTrueOrderByDisplayOrderAscIdAsc(MenuCategory category);
   long countByMenuHighlightTrue();
   List<MenuItem> findByMenuHighlightTrueAndAvailableTrueOrderByDisplayOrderAscIdAsc();
    Page<MenuItem> findByCategory(MenuCategory category, Pageable pageable);
    Page<MenuItem> findByNameContainingIgnoreCase(String q, Pageable pageable);
    Page<MenuItem> findByCategoryAndNameContainingIgnoreCase(MenuCategory category, String q, Pageable pageable);
    List<MenuItem> findByMenuHighlightTrueAndAvailableTrue(Sort sort);
}