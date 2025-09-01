package com.tasca.tasca_backend.dtos;

import com.tasca.tasca_backend.models.MenuCategory;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponseDTO {
    private Long id;
    private MenuCategory category;
    private String name;
    private String description;
    private double price;
    private String dietaryTags;
    private String allergens;
    private int spicyLevel;
    private boolean halal;
    private boolean available;
    private boolean featured;
    private boolean menuHighlight;
    private String menuHighlightTag;
    private String image;
    private Integer displayOrder;
}
