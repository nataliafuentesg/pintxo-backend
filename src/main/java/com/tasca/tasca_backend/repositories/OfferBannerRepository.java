package com.tasca.tasca_backend.repositories;

import com.tasca.tasca_backend.models.OfferBanner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OfferBannerRepository extends JpaRepository<OfferBanner, Long> {

    @Query("""
        select o from OfferBanner o
        where o.active = true
          and (:date is null or o.validFrom is null or o.validFrom <= :date)
          and (:date is null or o.validTo   is null or o.validTo   >= :date)
    """)
    List<OfferBanner> findActiveInDate(@Param("date") LocalDate date);

    List<OfferBanner> findByActiveTrue(Sort sort);
}