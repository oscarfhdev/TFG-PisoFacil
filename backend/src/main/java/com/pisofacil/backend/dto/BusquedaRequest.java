package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de entrada para los parámetros de búsqueda de habitaciones compatibles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusquedaRequest {

    private String ciudad;
    private BigDecimal precioMaximo;
}
