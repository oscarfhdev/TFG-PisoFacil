package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionDTO {

    private Long idHabitacion;
    private Long idPiso;
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

    // Datos del piso embebidos para evitar llamadas extra desde el frontend
    private String ciudad;
    private String direccion;
    private String codigoPostal;
}
