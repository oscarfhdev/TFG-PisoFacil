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
}
