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
public class Tapas5PublicResponse {
    private boolean active;
    private String title;
    private String windowText;
    private double price;
    private List<Tapas5ItemDTO> items;
}
