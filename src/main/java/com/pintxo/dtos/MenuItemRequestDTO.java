package com.pintxo.dtos;

import com.pintxo.models.MenuCategory;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequestDTO {
    private MenuCategory category; // Enum
    private String name;
    private String description;
    private double price;

    /** Pipes como en el front: "vegetarian|gluten-free" */
    private String dietaryTags;
    private String allergens;

    private int spicyLevel;    // 0..3
    private boolean halal;

    private Boolean available;       // opcional (si no lo mandas, no cambia)
    private Boolean featured;        // idem
    private Boolean menuHighlight;   // idem
    private String  menuHighlightTag;

    private String image; // URL absoluta (Netlify u otra)
    private Integer displayOrder;
}
