package com.pisofacil.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPasswordResetRequestDTO {
    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String passwordNueva;
}
