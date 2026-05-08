package com.pisofacil.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilUpdateRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    private LocalDate fechaNacimiento;
    private String genero;
    private String estudios;
    private String biografia;
    private String instagramUrl;
    private Boolean esFumador;
    private Boolean tieneMascota;
    private Boolean tienePareja;
    private Boolean perfilLgtbi;
    private String telefono;
    private String fotoPerfilUrl;
}
