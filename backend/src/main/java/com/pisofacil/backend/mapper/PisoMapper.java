package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.PisoRequestDTO;
import com.pisofacil.backend.dto.PisoResponseDTO;
import com.pisofacil.backend.model.Piso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PisoMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "idPiso", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "habitaciones", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    Piso toEntity(PisoRequestDTO dto);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    PisoResponseDTO toResponseDTO(Piso entity);

    List<PisoResponseDTO> toResponseDTOList(List<Piso> entities);
}
