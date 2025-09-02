package com.pintxo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkPublicItem {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean available;
    private boolean featured;

    @JsonProperty("order")
    private Integer displayOrder;

    private String image;
    private Double abv;
    private String region;
    private String grape;

    @JsonProperty("dietary_tags")
    private String dietaryTags;

    private String allergens;
}