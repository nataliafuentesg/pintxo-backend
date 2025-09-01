package com.tasca.tasca_backend.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferUpsertDTO {
    private String title;
    private String subtitle;
    private String imageUrl;
    private String badge;
    private String ctaPrimaryLabel;
    private String ctaPrimaryTo;
    private String ctaSecondaryLabel;
    private String ctaSecondaryTo;

    private LocalDate validFrom;
    private LocalDate validTo;
    private String daysOfWeek;  // "MON|TUE|WED..."

    private Boolean active;
    private Integer displayOrder;
}