package com.pisofacil.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") 
        String nombre,

        @NotBlank(message = "El email es obligatorio") 
        @Email(message = "Debe ser un email válido") 
        String email,

        @NotBlank(message = "La contraseña es obligatoria") 
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") 
        String password
) {
}
