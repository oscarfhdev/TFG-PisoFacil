package com.pisofacil.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponseDTO {
    private Long idReporte;
    private Long idUsuarioEmisor;
    private String nombreEmisor;
    private String categoria;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private String estado;
}
