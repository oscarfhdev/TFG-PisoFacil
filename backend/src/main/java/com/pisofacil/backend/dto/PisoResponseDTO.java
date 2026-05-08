package com.pisofacil.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PisoResponseDTO {
    private Long idPiso;
    private Long idUsuario;
    private String nombreUsuario; // Embedded flat user
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
    private String centroInteres;
    private LocalDateTime fechaCreacion;
}
