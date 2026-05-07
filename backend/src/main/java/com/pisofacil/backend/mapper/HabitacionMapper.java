package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.model.Habitacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.MappingTarget;
import com.pisofacil.backend.model.Foto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
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
    
    // Mapeos extraídos del Piso
    @Mapping(source = "piso.descripcionGlobal", target = "descripcionGlobal")
    @Mapping(source = "piso.numHabitacionesTotal", target = "numHabitacionesTotal")
    @Mapping(source = "piso.tieneWifi", target = "tieneWifi")
    @Mapping(source = "piso.tieneAscensor", target = "tieneAscensor")
    @Mapping(source = "piso.admiteMascotas", target = "admiteMascotas")
    @Mapping(source = "piso.admiteFumadores", target = "admiteFumadores")
    @Mapping(source = "piso.lgtbiFriendly", target = "lgtbiFriendly")
    
    // Mapeos extraídos del Propietario (Piso -> Usuario)
    @Mapping(source = "piso.usuario.idUsuario", target = "idUsuarioPropietario")
    @Mapping(source = "piso.usuario.nombre", target = "nombrePropietario")
    @Mapping(source = "piso.usuario.fotoPerfilUrl", target = "fotoPerfilUrlPropietario")
    @Mapping(source = "piso.usuario.instagramUrl", target = "instagramUrlPropietario")
    
    // Fotos se mapean en @AfterMapping
    @Mapping(target = "fotosHabitacion", ignore = true)
    @Mapping(target = "fotosPiso", ignore = true)
    HabitacionResponseDTO toResponseDTO(Habitacion entity);

    @AfterMapping
    default void mapFotos(Habitacion entity, @MappingTarget HabitacionResponseDTO dto) {
        if (entity.getFotos() != null) {
            dto.setFotosHabitacion(entity.getFotos().stream()
                    .map(Foto::getUrlAlmacenamiento)
                    .collect(Collectors.toList()));
        } else {
            dto.setFotosHabitacion(Collections.emptyList());
        }
        if (entity.getPiso() != null && entity.getPiso().getFotos() != null) {
            // Solo incluir las fotos del piso en sí (habitacion == null),
            // no las fotos que pertenecen a una habitación específica
            dto.setFotosPiso(entity.getPiso().getFotos().stream()
                    .filter(f -> f.getHabitacion() == null)
                    .map(Foto::getUrlAlmacenamiento)
                    .collect(Collectors.toList()));
        } else {
            dto.setFotosPiso(Collections.emptyList());
        }
    }

    List<HabitacionResponseDTO> toResponseDTOList(List<Habitacion> entities);
}
