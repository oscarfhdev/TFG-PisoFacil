package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.PublicarAnuncioRequestDTO;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnuncioMapper {

    @Mapping(target = "idPiso", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "habitaciones", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    Piso toPiso(PublicarAnuncioRequestDTO dto);

    @Mapping(source = "tituloAnuncioHabitacion", target = "tituloAnuncio")
    @Mapping(source = "precioMensualHabitacion", target = "precioMensual")
    @Mapping(source = "descripcionEspecificaHabitacion", target = "descripcionEspecifica")
    @Mapping(source = "superficieM2Habitacion", target = "superficieM2")
    @Mapping(source = "tieneBanoPrivadoHabitacion", target = "tieneBanoPrivado")
    @Mapping(source = "amuebladaHabitacion", target = "amueblada")
    @Mapping(source = "exteriorHabitacion", target = "exterior")
    @Mapping(source = "tieneCalefaccionHabitacion", target = "tieneCalefaccion")
    @Mapping(source = "tieneAireAcondicionadoHabitacion", target = "tieneAireAcondicionado")
    @Mapping(target = "idHabitacion", ignore = true)
    @Mapping(target = "piso", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    @Mapping(target = "estaDisponible", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "favoritos", ignore = true)
    Habitacion toHabitacion(PublicarAnuncioRequestDTO dto);
}
