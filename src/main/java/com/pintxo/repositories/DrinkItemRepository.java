package com.pintxo.repositories;

import com.pintxo.models.DrinkCategory;
import com.pintxo.models.DrinkItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrinkItemRepository extends JpaRepository<DrinkItem, Long> {

    @Query("""
      SELECT d FROM DrinkItem d
      WHERE (:category IS NULL OR d.category = :category)
        AND (
          :q IS NULL OR :q = '' OR
          LOWER(CONCAT(COALESCE(d.name,''),' ',COALESCE(d.description,''),' ',COALESCE(d.dietaryTags,''),' ',COALESCE(d.allergens,''),' ',COALESCE(d.region,''),' ',COALESCE(d.grape,'')))
          LIKE LOWER(CONCAT('%', :q, '%'))
        )
      ORDER BY d.displayOrder NULLS LAST, d.id DESC
    """)
    Page<DrinkItem> search(@Param("category") DrinkCategory category,
                           @Param("q") String q,
                           Pageable pageable);

    List<DrinkItem> findByAvailableTrueOrderByCategoryAscDisplayOrderAscIdAsc();
}