package com.pintxo.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuHighlightCardDTO {
    private Long id;
    private String name;
    private String desc;
    private String price;
    private String image;
    private String tag;
}
