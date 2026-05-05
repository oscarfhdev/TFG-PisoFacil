package com.pisofacil.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FotoRequestDTO {
    @NotNull(message = "El id del piso es obligatorio")
    private Long idPiso;

    private Long idHabitacion;

    @NotBlank(message = "La URL de almacenamiento es obligatoria")
    private String urlAlmacenamiento;
}
