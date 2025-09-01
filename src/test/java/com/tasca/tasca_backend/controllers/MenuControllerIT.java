package com.tasca.tasca_backend.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MenuControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    void full_returnsCurrencyAndCategoriesAndItemsWithExpectedKeys() throws Exception {
        mvc.perform(get("/api/menu/full").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", not(emptyOrNullString())))
                .andExpect(jsonPath("$.categories", not(empty())))
                // al menos una categoría con name/slug/items
                .andExpect(jsonPath("$.categories[0].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$.categories[0].slug", not(emptyOrNullString())))
                .andExpect(jsonPath("$.categories[0].items", isA(java.util.List.class)))
                // un item con todas las claves que espera el front (snake-case incluidas)
                .andExpect(jsonPath("$.categories[0].items[0].id").exists())
                .andExpect(jsonPath("$.categories[0].items[0].name").exists())
                .andExpect(jsonPath("$.categories[0].items[0].description").exists())
                .andExpect(jsonPath("$.categories[0].items[0].price").exists())
                .andExpect(jsonPath("$.categories[0].items[0].dietary_tags").exists())
                .andExpect(jsonPath("$.categories[0].items[0].allergens").exists())
                .andExpect(jsonPath("$.categories[0].items[0].spicy_level").exists())
                .andExpect(jsonPath("$.categories[0].items[0].halal").exists())
                .andExpect(jsonPath("$.categories[0].items[0].available").exists())
                .andExpect(jsonPath("$.categories[0].items[0].featured").exists())
                .andExpect(jsonPath("$.categories[0].items[0].image").exists())
                .andExpect(jsonPath("$.categories[0].items[0].order").exists());
    }
}