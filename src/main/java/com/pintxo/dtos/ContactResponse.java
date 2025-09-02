package com.pintxo.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponse {
    private boolean ok;
    private Long id;
}
