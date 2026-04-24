package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long idUsuario;
    private String nombre;
    private String email;
    private Boolean esAdmin;
    private LocalDate fechaNacimiento;
    private String genero;
    private String estudios;
    private String biografia;
    private String fotoPerfilUrl;
    private String instagramUrl;
    private LocalDateTime fechaRegistro;

    // Rasgos de compatibilidad
    private Boolean esFumador;
    private Boolean tieneMascota;
    private Boolean tienePareja;
    private Boolean perfilLgtbi;
}
