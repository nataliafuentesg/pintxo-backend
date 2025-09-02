package com.pintxo.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUpdateRequest {
    private String status;  // NEW, CONTACTED, CLOSED
    private String notes;
}
