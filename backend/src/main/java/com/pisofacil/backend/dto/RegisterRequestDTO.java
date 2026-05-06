package com.pisofacil.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
        @NotBlank(message = "El nombre es obligatorio") 
        private String nombre;

        private String apellidos;

        @NotBlank(message = "El email es obligatorio") 
        @Email(message = "Debe ser un email válido") 
        private String email;

        @NotBlank(message = "La contraseña es obligatoria") 
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") 
        private String password;

        private LocalDate fechaNacimiento;
        private String estudios;
        private String biografia;
        private Boolean esFumador;
        private Boolean tieneMascota;
        private Boolean tienePareja;
        private Boolean perfilLgtbi;
}
