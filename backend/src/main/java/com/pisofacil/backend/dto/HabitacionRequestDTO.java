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
public class HabitacionRequestDTO {
    @NotNull(message = "El id del piso es obligatorio")
    private Long idPiso;

    @NotBlank(message = "El título es obligatorio")
    private String tituloAnuncio;

    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precioMensual;

    private String descripcionEspecifica;
    private Boolean estaDisponible;
    private Double superficieM2;
    private Boolean tieneBanoPrivado;
    private Boolean amueblada;
    private Boolean exterior;
    private Boolean tieneCalefaccion;
    private Boolean tieneAireAcondicionado;
}
