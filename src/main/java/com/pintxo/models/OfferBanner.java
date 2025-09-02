package com.pintxo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "offer_banners")
public class OfferBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Content
    @Column(nullable = false)
    private String title;

    @Column(length = 600)
    private String subtitle;

    @Column(nullable = false)
    private String imageUrl;

    private String badge;

    private String ctaPrimaryLabel;
    private String ctaPrimaryTo;

    private String ctaSecondaryLabel;
    private String ctaSecondaryTo;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String daysOfWeek;
    @Builder.Default
    private boolean active = true;
    private Integer displayOrder;
}