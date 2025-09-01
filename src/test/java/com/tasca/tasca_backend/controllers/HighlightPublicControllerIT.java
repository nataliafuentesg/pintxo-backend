package com.tasca.tasca_backend.controllers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HighlightPublicControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    void highlights_default_today_returnsCardsWithExpectedShape() throws Exception {
        mvc.perform(get("/api/menu/highlights").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                // card shape
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].desc").exists())
                .andExpect(jsonPath("$[0].price", startsWith("$"))) // formateado
                .andExpect(jsonPath("$[0].image").exists())
                .andExpect(jsonPath("$[0].tag").exists()); // puede ser null en JSON
    }

    @Test
    void highlights_byDate_returnsProgrammedItemsIfAny() throws Exception {
        // ajusta la fecha a una que tengas programada en data.sql (ej: 2025-08-14)
        String date = LocalDate.of(2025, 8, 14).toString();

        mvc.perform(get("/api/menu/highlights")
                        .param("date", date)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                // ordenado por position asc (como guardamos en menuhighlightday)
                .andExpect(jsonPath("$[0].name", not(emptyOrNullString())));
    }

    @Test
    void highlights_limit_capsTheNumberOfCards() throws Exception {
        mvc.perform(get("/api/menu/highlights")
                        .param("limit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(lessThanOrEqualTo(2))));
    }
}