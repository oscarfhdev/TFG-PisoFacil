package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FotoDTO {

    private Long idFoto;
    private Long idPiso;
    private Long idHabitacion;
    private String urlAlmacenamiento;
}
