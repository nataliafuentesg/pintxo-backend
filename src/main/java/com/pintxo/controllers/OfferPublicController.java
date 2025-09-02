package com.pintxo.controllers;

import com.pintxo.dtos.OfferSlideResponse;
import com.pintxo.services.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/offers")
public class OfferPublicController {

    private final OfferService service;

    @GetMapping
    public ResponseEntity<List<OfferSlideResponse>> list(
            @RequestParam(required = false) String date,   // yyyy-MM-dd
            @RequestParam(required = false) String tz,     // opcional
            @RequestParam(required = false) Integer limit
    ) {
        LocalDate d = null;
        if (date != null && !date.isBlank()) d = LocalDate.parse(date);
        else if (tz != null && !tz.isBlank()) d = LocalDate.now(ZoneId.of(tz));
        return ResponseEntity.ok(service.publicOffers(d, limit));
    }
}
