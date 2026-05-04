package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.FavoritoRequestDTO;
import com.pisofacil.backend.dto.FavoritoResponseDTO;
import com.pisofacil.backend.model.Favorito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoritoMapper {

    @Mapping(target = "idFavorito", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "habitacion", ignore = true)
    Favorito toEntity(FavoritoRequestDTO dto);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "habitacion.idHabitacion", target = "idHabitacion")
    @Mapping(source = "habitacion.tituloAnuncio", target = "tituloAnuncio")
    @Mapping(source = "habitacion.precioMensual", target = "precioMensual")
    @Mapping(source = "habitacion.piso.ciudad", target = "ciudad")
    FavoritoResponseDTO toResponseDTO(Favorito entity);

    List<FavoritoResponseDTO> toResponseDTOList(List<Favorito> entities);
}
