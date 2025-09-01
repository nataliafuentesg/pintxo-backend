package com.tasca.tasca_backend.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String dietaryTags;
    private String allergens;
    private int spicyLevel;
    private boolean halal;
    private boolean available;
    private boolean featured;
    private String image;
    private int order;
}
