package com.pintxo.services;
import com.pintxo.dtos.OfferSlideResponse;
import com.pintxo.dtos.OfferUpsertDTO;
import com.pintxo.models.OfferBanner;
import com.pintxo.repositories.OfferBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final OfferBannerRepository repo;

    public List<OfferSlideResponse> publicOffers(LocalDate date, Integer limit) {
        LocalDate d = (date != null) ? date : LocalDate.now(ZoneId.of("America/New_York"));
        String dow = d.getDayOfWeek().name().substring(0, 3).toLowerCase(); // mon/tue/...

        int max = (limit == null || limit <= 0) ? 5 : limit;

        List<OfferBanner> base;
        try {
            base = repo.findActiveInDate(d);
        } catch (Exception e) {
            // Si la JPQL fallara por tipos, cast, etc. en prod → no tumbamos el endpoint
            log.error("findActiveInDate() failed for date {}. Falling back to active=true list. Cause: {}",
                    d, e.toString(), e);
            base = repo.findByActiveTrue(Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.desc("id")));
        }

        List<OfferBanner> filtered = base.stream()
                .filter(o -> {
                    String days = (o.getDaysOfWeek() == null) ? "" : o.getDaysOfWeek().toLowerCase();
                    return days.isBlank() || days.contains(dow);
                })
                .sorted(Comparator
                        .comparing((OfferBanner o) -> o.getDisplayOrder() == null ? 999 : o.getDisplayOrder())
                        .thenComparing(OfferBanner::getId, Comparator.reverseOrder()))
                .limit(max)
                .toList();

        return filtered.stream().map(this::toSlide).toList();
    }

    private OfferSlideResponse toSlide(OfferBanner o) {
        return new OfferSlideResponse(
                o.getId(),
                o.getTitle(),
                o.getSubtitle(),
                o.getImageUrl(),
                o.getBadge(),
                nvl(o.getCtaPrimaryLabel(), "View Menu"),
                nvl(o.getCtaPrimaryTo(), "/menu"),
                nvl(o.getCtaSecondaryLabel(), "Book a Table"),
                nvl(o.getCtaSecondaryTo(), "/reservations")
        );
    }

    private static String nvl(String v, String d) { return (v == null || v.isBlank()) ? d : v; }

    public Optional<OfferBanner> findById(Long id) {
        return repo.findById(id);
    }

    public Page<OfferBanner> listAdmin(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.desc("id")));
        Page<OfferBanner> all = repo.findAll(pageable);
        if (q == null || q.isBlank()) return all;

        List<OfferBanner> filtered = all.getContent().stream()
                .filter(o -> (o.getTitle() + " " + Optional.ofNullable(o.getSubtitle()).orElse(""))
                        .toLowerCase().contains(q.toLowerCase()))
                .toList();
        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    public OfferBanner create(OfferUpsertDTO dto) {
        return repo.save(apply(new OfferBanner(), dto));
    }

    public OfferBanner update(Long id, OfferUpsertDTO dto) {
        OfferBanner o = repo.findById(id).orElseThrow();
        return repo.save(apply(o, dto));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void setActive(Long id, boolean value) {
        OfferBanner o = repo.findById(id).orElseThrow();
        o.setActive(value);
        repo.save(o);
    }

    private OfferBanner apply(OfferBanner o, OfferUpsertDTO dto) {
        o.setTitle(dto.getTitle());
        o.setSubtitle(dto.getSubtitle());
        o.setImageUrl(dto.getImageUrl());
        o.setBadge(dto.getBadge());
        o.setCtaPrimaryLabel(dto.getCtaPrimaryLabel());
        o.setCtaPrimaryTo(dto.getCtaPrimaryTo());
        o.setCtaSecondaryLabel(dto.getCtaSecondaryLabel());
        o.setCtaSecondaryTo(dto.getCtaSecondaryTo());
        o.setValidFrom(dto.getValidFrom());
        o.setValidTo(dto.getValidTo());
        o.setDaysOfWeek(dto.getDaysOfWeek());
        if (dto.getActive() != null) o.setActive(dto.getActive());
        o.setDisplayOrder(dto.getDisplayOrder());
        return o;
    }
}
