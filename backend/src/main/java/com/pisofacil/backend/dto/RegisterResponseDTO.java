package com.pisofacil.backend.dto;

public record RegisterResponseDTO(
        Long idUsuario,
        String nombre,
        String email,
        String message
) {
}
