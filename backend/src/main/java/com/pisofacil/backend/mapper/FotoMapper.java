package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.FotoRequestDTO;
import com.pisofacil.backend.dto.FotoResponseDTO;
import com.pisofacil.backend.model.Foto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FotoMapper {

    @Mapping(target = "idFoto", ignore = true)
    @Mapping(target = "piso", ignore = true)
    @Mapping(target = "habitacion", ignore = true)
    Foto toEntity(FotoRequestDTO dto);

    @Mapping(source = "piso.idPiso", target = "idPiso")
    @Mapping(source = "habitacion.idHabitacion", target = "idHabitacion")
    FotoResponseDTO toResponseDTO(Foto entity);

    List<FotoResponseDTO> toResponseDTOList(List<Foto> entities);
}
