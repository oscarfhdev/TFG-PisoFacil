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
public class PisoDTO {

    private Long idPiso;
    private Long idUsuario;
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
    private LocalDateTime fechaCreacion;
}
