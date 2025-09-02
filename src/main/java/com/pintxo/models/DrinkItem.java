package com.pintxo.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "drink_items")
public class DrinkItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private DrinkCategory category;

    @Column(nullable = false)
    private String name;

    @Column(length = 600)
    private String description;

    @Column(nullable = false)
    private double price;

    /** pipes: "cocktail|by-glass|spanish" */
    private String dietaryTags;

    /** pipes: "sulfites|gluten" */
    private String allergens;

    private Double abv;       // % alcohol
    private String region;    // e.g. "Rioja, Spain"
    private String grape;     // e.g. "Tempranillo"

    @Builder.Default
    private boolean available = true;

    @Builder.Default
    private boolean featured = false;

    private String image;

    @Column(name = "display_order")
    private Integer displayOrder;
}