package com.tasca.tasca_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {
    @NotBlank @Size(max=120)  private String name;
    @NotBlank @Email @Size(max=180) private String email;
    @Size(max=32)   private String phone;
    @Size(max=180)  private String subject;
    @NotBlank @Size(min=10, max=4000) private String message;

}
