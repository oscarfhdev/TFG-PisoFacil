package com.pisofacil.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "El email es obligatorio") 
        @Email(message = "Debe ser un email válido") 
        String email,
        
        @NotBlank(message = "La contraseña es obligatoria") 
        String password
) {
}
