package com.pisofacil.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PisoRequestDTO {
    @NotNull(message = "El id de usuario es obligatorio")
    private Long idUsuario;

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
    private String centroInteres;
}
