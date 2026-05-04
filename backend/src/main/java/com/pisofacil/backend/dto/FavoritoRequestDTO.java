package com.pisofacil.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoRequestDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El id de la habitación es obligatorio")
    private Long idHabitacion;
}
