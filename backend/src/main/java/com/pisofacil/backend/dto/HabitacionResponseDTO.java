package com.pisofacil.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionResponseDTO {
    private Long idHabitacion;
    private Long idPiso;
    private String ciudad; // From Piso
    private String direccion; // From Piso
    private String tituloAnuncio;
    private BigDecimal precioMensual;
    private String descripcionEspecifica;
    private Boolean estaDisponible;
    private LocalDateTime fechaPublicacion;
    private Double superficieM2;
    private Boolean tieneBanoPrivado;
    private Boolean amueblada;
    private Boolean exterior;
    private Boolean tieneCalefaccion;
    private Boolean tieneAireAcondicionado;
}
