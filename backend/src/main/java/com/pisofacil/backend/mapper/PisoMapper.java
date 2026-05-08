package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.PisoRequestDTO;
import com.pisofacil.backend.dto.PisoResponseDTO;
import com.pisofacil.backend.model.Piso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import com.pisofacil.backend.dto.MisPisosResponseDTO;

@Mapper(componentModel = "spring", uses = {HabitacionMapper.class})
public interface PisoMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "idPiso", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "habitaciones", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    Piso toEntity(PisoRequestDTO dto);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(target = "nombreUsuario", expression = "java(entity.getUsuario().getNombre() + (entity.getUsuario().getApellidos() != null ? \" \" + entity.getUsuario().getApellidos() : \"\"))")
    PisoResponseDTO toResponseDTO(Piso entity);

    List<PisoResponseDTO> toResponseDTOList(List<Piso> entities);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(target = "nombreUsuario", expression = "java(entity.getUsuario().getNombre() + (entity.getUsuario().getApellidos() != null ? \" \" + entity.getUsuario().getApellidos() : \"\"))")
    MisPisosResponseDTO toMisPisosResponseDTO(Piso entity);

    List<MisPisosResponseDTO> toMisPisosResponseDTOList(List<Piso> entities);
}
