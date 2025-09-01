package com.tasca.tasca_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuFullResponse {
    private String currency;
    private List<MenuCategoryDTO> categories;
}
