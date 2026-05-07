package com.pisofacil.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicarAnuncioRequestDTO {

    // Campos de Piso
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
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

    // Campos de Habitación (La primera habitación)
    @NotBlank(message = "El título del anuncio de la habitación es obligatorio")
    private String tituloAnuncioHabitacion;

    @NotNull(message = "El precio de la habitación es obligatorio")
    private BigDecimal precioMensualHabitacion;

    private String descripcionEspecificaHabitacion;
    private Double superficieM2Habitacion;
    private Boolean tieneBanoPrivadoHabitacion;
    private Boolean amuebladaHabitacion;
    private Boolean exteriorHabitacion;
    private Boolean tieneCalefaccionHabitacion;
    private Boolean tieneAireAcondicionadoHabitacion;
}
