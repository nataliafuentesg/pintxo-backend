package com.pintxo.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EventPublicDTO {
    private Long id;
    private String type;       // "event" | "special"  (minúsculas para el front)
    private String date;       // "Fri · 7:00 PM" | "Today · All Day"
    private String title;
    private String desc;
    private String image;
    private String ctaTo;      // link destino
}
