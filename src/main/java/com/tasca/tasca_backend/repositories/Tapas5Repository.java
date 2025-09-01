package com.tasca.tasca_backend.repositories;

import com.tasca.tasca_backend.models.Tapas5;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Tapas5Repository extends JpaRepository<Tapas5, Long> {
    Optional<Tapas5> findByCode(String code);
}
