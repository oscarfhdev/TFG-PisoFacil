package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDTO {

    private Long idReporte;
    private Long idUsuarioEmisor;
    private String categoria;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private String estado;
}
