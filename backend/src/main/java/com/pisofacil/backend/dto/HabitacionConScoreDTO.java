package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que incluye los datos de una habitación + su puntuación de compatibilidad.
 * Se usa en los resultados de búsqueda para que el frontend pueda mostrar el score.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionConScoreDTO {

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

    // Datos del piso embebidos
    private String ciudad;
    private String direccion;
    private String codigoPostal;

    // Score de compatibilidad (0-130 aprox)
    private int compatibilidad;
}
