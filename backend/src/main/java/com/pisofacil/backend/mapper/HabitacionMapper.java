package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.model.Habitacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {

    @Mapping(target = "piso", ignore = true)
    @Mapping(target = "idHabitacion", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "favoritos", ignore = true)
    Habitacion toEntity(HabitacionRequestDTO dto);

    @Mapping(source = "piso.idPiso", target = "idPiso")
    @Mapping(source = "piso.ciudad", target = "ciudad")
    @Mapping(source = "piso.direccion", target = "direccion")
    HabitacionResponseDTO toResponseDTO(Habitacion entity);

    List<HabitacionResponseDTO> toResponseDTOList(List<Habitacion> entities);
}
