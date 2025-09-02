package com.pintxo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tapas5ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Set<String> allergens = new LinkedHashSet<>(); // ⬅ Texto libre
    private Set<String> tags = new LinkedHashSet<>();
    private boolean available;
}
