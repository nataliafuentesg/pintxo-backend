package com.tasca.tasca_backend.repositories;

import com.tasca.tasca_backend.models.Tapas5Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Tapas5ItemRepository extends JpaRepository<Tapas5Item, Long> {
    List<Tapas5Item> findAllByTapasIdAndAvailableTrueOrderByNameAsc(Long tapasId);
    List<Tapas5Item> findAllByTapasIdOrderByNameAsc(Long tapasId);
}
