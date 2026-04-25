package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de entrada para actualizar un anuncio existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnuncioUpdateRequest {

    // ── Datos de la Habitación (editables) ──
    private String tituloAnuncio;
    private BigDecimal precioMensual;
    private String descripcionEspecifica;
    private Double superficieM2;
    private Boolean tieneBanoPrivado;
    private Boolean amueblada;
    private Boolean exterior;
    private Boolean tieneCalefaccion;
    private Boolean tieneAireAcondicionado;

    // ── Datos del Piso (editables) ──
    private String descripcionGlobal;
    private Boolean tieneWifi;
    private Boolean tieneAscensor;
    private Boolean admiteFumadores;
    private Boolean admiteMascotas;
    private Boolean admiteParejas;
    private Boolean lgtbiFriendly;
}
