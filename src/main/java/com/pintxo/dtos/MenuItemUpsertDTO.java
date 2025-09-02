package com.pintxo.dtos;

import com.pintxo.models.MenuCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemUpsertDTO {
    private MenuCategory category;
    private String name;
    private String description;
    private double price;
    private String dietaryTags;    // "vegetarian|gluten-free"
    private String allergens;
    private int spicyLevel;        // 0..3
    private boolean halal;
    private String image;          // URL absoluta (Netlify u otra)
    private Integer displayOrder;

    // Opcionales (si no vienen, no cambian)
    private Boolean available;
    private Boolean featured;
    private Boolean menuHighlight;
    private String  menuHighlightTag;
}
