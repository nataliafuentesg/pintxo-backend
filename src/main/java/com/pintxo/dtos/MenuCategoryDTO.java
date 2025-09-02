package com.pintxo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategoryDTO {
    private String name;
    private String slug;
    private List<MenuItemDTO> items;
}
