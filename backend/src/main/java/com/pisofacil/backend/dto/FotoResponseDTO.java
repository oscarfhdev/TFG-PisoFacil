package com.pisofacil.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FotoResponseDTO {
    private Long idFoto;
    private Long idPiso;
    private Long idHabitacion;
    private String urlAlmacenamiento;
}
