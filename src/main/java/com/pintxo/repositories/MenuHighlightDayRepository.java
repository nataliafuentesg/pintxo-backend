package com.pintxo.repositories;
import com.pintxo.models.MenuHighlightDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuHighlightDayRepository extends JpaRepository<MenuHighlightDay, Long> {
    List<MenuHighlightDay> findByDateAndActiveTrueOrderByPositionAscIdAsc(LocalDate date);
    List<MenuHighlightDay> findByDateAndActiveTrueOrderByPositionAsc(LocalDate date);
    Page<MenuHighlightDay> findByDate(LocalDate date, Pageable pageable);
    List<MenuHighlightDay> findByDateOrderByPositionAsc(LocalDate date);
    Optional<MenuHighlightDay> findByDateAndItem_Id(LocalDate date, Long itemId);
}
