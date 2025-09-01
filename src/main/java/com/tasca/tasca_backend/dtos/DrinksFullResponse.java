package com.tasca.tasca_backend.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinksFullResponse {
    @Builder.Default
    private String currency = "USD";

    private List<CategoryBlock> categories;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CategoryBlock {
        private String name; // displayName
        private String slug;
        private List<DrinkPublicItem> items;
    }
}