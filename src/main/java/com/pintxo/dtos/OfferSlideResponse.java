package com.pintxo.dtos;

public record OfferSlideResponse(
        Long id,
        String title,
        String subtitle,
        String imageUrl,
        String badge,
        String ctaPrimaryLabel,
        String ctaPrimaryTo,
        String ctaSecondaryLabel,
        String ctaSecondaryTo
) {}