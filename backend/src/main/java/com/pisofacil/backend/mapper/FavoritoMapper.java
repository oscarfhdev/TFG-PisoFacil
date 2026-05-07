package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.FavoritoRequestDTO;
import com.pisofacil.backend.dto.FavoritoResponseDTO;
import com.pisofacil.backend.model.Favorito;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    @Mapping(source = "habitacion.piso.direccion", target = "direccion")
    @Mapping(source = "habitacion.superficieM2", target = "superficieM2")
    @Mapping(source = "habitacion.estaDisponible", target = "estaDisponible")
    @Mapping(source = "habitacion.piso.admiteMascotas", target = "admiteMascotas")
    @Mapping(source = "habitacion.piso.admiteFumadores", target = "admiteFumadores")
    @Mapping(source = "habitacion.piso.lgtbiFriendly", target = "lgtbiFriendly")
    @Mapping(source = "habitacion.piso.usuario.fotoPerfilUrl", target = "fotoPerfilUrlPropietario")
    @Mapping(target = "fotoPrincipal", ignore = true)
    FavoritoResponseDTO toResponseDTO(Favorito entity);

    @AfterMapping
    default void mapFotoPrincipal(Favorito entity, @MappingTarget FavoritoResponseDTO dto) {
        if (entity.getHabitacion() != null) {
            if (entity.getHabitacion().getFotos() != null && !entity.getHabitacion().getFotos().isEmpty()) {
                dto.setFotoPrincipal(entity.getHabitacion().getFotos().get(0).getUrlAlmacenamiento());
            } else if (entity.getHabitacion().getPiso() != null && 
                       entity.getHabitacion().getPiso().getFotos() != null && 
                       !entity.getHabitacion().getPiso().getFotos().isEmpty()) {
                dto.setFotoPrincipal(entity.getHabitacion().getPiso().getFotos().get(0).getUrlAlmacenamiento());
            } else {
                dto.setFotoPrincipal("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800");
            }
        }
    }

    List<FavoritoResponseDTO> toResponseDTOList(List<Favorito> entities);
}
