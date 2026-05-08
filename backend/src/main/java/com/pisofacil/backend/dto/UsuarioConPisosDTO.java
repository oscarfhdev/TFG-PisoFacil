package com.pisofacil.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioConPisosDTO {
    private Long idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String fotoPerfilUrl;
    private Long cantidadPisos;
}
