package com.tasca.tasca_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HighlightDayResponseDTO {
    private Long id;
    private LocalDate date;
    private Long itemId;
    private String itemName;
    private Integer position;
    private String tagOverride;
    private boolean active;
}
