package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.ReporteRequestDTO;
import com.pisofacil.backend.dto.ReporteResponseDTO;
import com.pisofacil.backend.model.Reporte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    @Mapping(target = "idReporte", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "usuarioEmisor", ignore = true)
    Reporte toEntity(ReporteRequestDTO dto);

    @Mapping(source = "usuarioEmisor.idUsuario", target = "idUsuarioEmisor")
    @Mapping(source = "usuarioEmisor.nombre", target = "nombreEmisor")
    ReporteResponseDTO toResponseDTO(Reporte entity);

    List<ReporteResponseDTO> toResponseDTOList(List<Reporte> entities);
}
