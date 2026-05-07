package com.pisofacil.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoResponseDTO {
    private Long idFavorito;
    private Long idUsuario;
    private Long idHabitacion;
    private String tituloAnuncio;
    private BigDecimal precioMensual;
    private String ciudad;

    // --- CAMPOS ENRIQUECIDOS PARA ANUNCIOCARD ---
    private String direccion;
    private String fotoPrincipal;
    private Double superficieM2;
    private Boolean estaDisponible;
    private Boolean admiteMascotas;
    private Boolean admiteFumadores;
    private Boolean lgtbiFriendly;
    private String fotoPerfilUrlPropietario;
}
