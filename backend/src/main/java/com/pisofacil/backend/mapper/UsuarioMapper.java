package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.UsuarioRequestDTO;
import com.pisofacil.backend.dto.UsuarioResponseDTO;
import com.pisofacil.backend.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "esAdmin", ignore = true)
    @Mapping(target = "pisos", ignore = true)
    @Mapping(target = "favoritos", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "cuentaActiva", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario entity);

    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> entities);
}
