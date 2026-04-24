package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de entrada para publicar un anuncio completo (Piso + Habitación + Fotos).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnuncioRequest {

    // ── Datos del Piso ──
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private Integer numHabitacionesTotal;
    private Integer numBanos;
    private Integer planta;
    private Double superficieTotalM2;
    private Boolean tieneWifi;
    private Boolean tieneAscensor;
    private String descripcionGlobal;
    private Boolean admiteFumadores;
    private Boolean admiteMascotas;
    private Boolean admiteParejas;
    private Boolean lgtbiFriendly;

    // ── Datos de la Habitación ──
    private String tituloAnuncio;
    private BigDecimal precioMensual;
    private String descripcionEspecifica;
    private Double superficieM2;
    private Boolean tieneBanoPrivado;
    private Boolean amueblada;
    private Boolean exterior;
    private Boolean tieneCalefaccion;
    private Boolean tieneAireAcondicionado;

    // ── Fotos (URLs) ──
    private List<String> fotosUrls;
}
