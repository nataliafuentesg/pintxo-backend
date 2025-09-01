package com.tasca.tasca_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkItemUpsertDTO {
    @NotBlank
    private String category;        // enum name, e.g. "WINE_BOTTLES"

    @NotBlank
    private String name;

    private String description;

    @PositiveOrZero
    private double price;

    private String dietaryTags;     // pipes
    private String allergens;       // pipes
    private Double abv;
    private String region;
    private String grape;

    private Boolean available;
    private Boolean featured;
    private String image;
    private Integer displayOrder;
}