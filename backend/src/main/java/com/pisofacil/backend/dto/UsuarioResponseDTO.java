package com.pisofacil.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
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
    private Boolean esFumador;
    private Boolean tieneMascota;
    private Boolean tienePareja;
    private Boolean perfilLgtbi;
}
