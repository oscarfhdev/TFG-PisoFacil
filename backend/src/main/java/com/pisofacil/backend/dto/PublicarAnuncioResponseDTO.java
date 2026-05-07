package com.pisofacil.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicarAnuncioResponseDTO {
    private String mensaje;
    private PisoResponseDTO piso;
    private HabitacionResponseDTO habitacion;
}
