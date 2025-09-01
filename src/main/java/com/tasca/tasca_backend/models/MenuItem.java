package com.tasca.tasca_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private MenuCategory category; // Enum en lugar de tabla Category

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private double price;

    /** Pipes como en el front: "vegetarian|gluten-free" */
    private String dietaryTags;
    private String allergens;

    private int spicyLevel;            // 0..3
    private boolean halal;

    @Builder.Default
    private boolean available = true;

    @Builder.Default
    private boolean featured = false;

    @Builder.Default
    private boolean menuHighlight = false;

    private String menuHighlightTag;

    private String image;

    @Column(name = "display_order")
    private Integer displayOrder;
}

