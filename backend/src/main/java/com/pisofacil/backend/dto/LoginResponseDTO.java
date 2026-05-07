package com.pisofacil.backend.dto;

public record LoginResponseDTO(
        String token,
        Long idUsuario,
        String nombre,
        String email,
        String role,
        String fotoPerfilUrl
) {
}
