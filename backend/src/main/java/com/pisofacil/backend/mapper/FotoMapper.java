package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.FotoDTO;
import com.pisofacil.backend.model.Foto;

public class FotoMapper {

    private FotoMapper() {
    }

    public static FotoDTO toDTO(Foto foto) {
        if (foto == null) return null;

        return FotoDTO.builder()
                .idFoto(foto.getIdFoto())
                .idPiso(foto.getPiso() != null ? foto.getPiso().getIdPiso() : null)
                .idHabitacion(foto.getHabitacion() != null ? foto.getHabitacion().getIdHabitacion() : null)
                .urlAlmacenamiento(foto.getUrlAlmacenamiento())
                .build();
    }
}
