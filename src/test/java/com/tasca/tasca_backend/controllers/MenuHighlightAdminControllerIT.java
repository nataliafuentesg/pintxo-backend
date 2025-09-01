package com.tasca.tasca_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MenuHighlightAdminControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void upsert_replacesHighlightsForDate_andGetReturnsInOrder() throws Exception {
        // Prepara un body para el 2025-08-15 con dos items (asegúrate que estos IDs existan en tu data.sql)
        Map<String, Object> body = Map.of(
                "date", "2025-08-15",
                "items", List.of(
                        Map.of("itemId", 3001, "position", 2, "tag", "Chef’s Pick"), // Gambas
                        Map.of("itemId", 1001, "position", 1, "tag", "Vegetarian")   // Patatas
                )
        );

        mvc.perform(put("/api/menu/admin/highlights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.count").value(2));

        // Ahora GET por esa fecha, debe salir en orden de position: id=1001 primero (position 1)
        mvc.perform(get("/api/menu/highlights")
                        .param("date", "2025-08-15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1001))
                .andExpect(jsonPath("$[0].tag").value("Vegetarian"))
                .andExpect(jsonPath("$[1].id").value(3001))
                .andExpect(jsonPath("$[1].tag").value("Chef’s Pick"));
    }
}
