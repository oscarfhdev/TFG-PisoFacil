package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.AnuncioRequest;
import com.pisofacil.backend.dto.HabitacionDTO;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;

public class HabitacionMapper {

    private HabitacionMapper() {
    }

    /**
     * Convierte una Habitacion a DTO, incluyendo datos del piso (ciudad, dirección).
     */
    public static HabitacionDTO toDTO(Habitacion habitacion) {
        if (habitacion == null) return null;

        Piso piso = habitacion.getPiso();

        return HabitacionDTO.builder()
                .idHabitacion(habitacion.getIdHabitacion())
                .idPiso(piso != null ? piso.getIdPiso() : null)
                .tituloAnuncio(habitacion.getTituloAnuncio())
                .precioMensual(habitacion.getPrecioMensual())
                .descripcionEspecifica(habitacion.getDescripcionEspecifica())
                .estaDisponible(habitacion.getEstaDisponible())
                .fechaPublicacion(habitacion.getFechaPublicacion())
                .superficieM2(habitacion.getSuperficieM2())
                .tieneBanoPrivado(habitacion.getTieneBanoPrivado())
                .amueblada(habitacion.getAmueblada())
                .exterior(habitacion.getExterior())
                .tieneCalefaccion(habitacion.getTieneCalefaccion())
                .tieneAireAcondicionado(habitacion.getTieneAireAcondicionado())
                // Datos del piso embebidos
                .ciudad(piso != null ? piso.getCiudad() : null)
                .direccion(piso != null ? piso.getDireccion() : null)
                .codigoPostal(piso != null ? piso.getCodigoPostal() : null)
                .build();
    }

    /**
     * Extrae los campos de la habitación desde un AnuncioRequest.
     */
    public static Habitacion fromAnuncioRequest(AnuncioRequest request) {
        if (request == null) return null;

        return Habitacion.builder()
                .tituloAnuncio(request.getTituloAnuncio())
                .precioMensual(request.getPrecioMensual())
                .descripcionEspecifica(request.getDescripcionEspecifica())
                .superficieM2(request.getSuperficieM2())
                .tieneBanoPrivado(request.getTieneBanoPrivado())
                .amueblada(request.getAmueblada())
                .exterior(request.getExterior())
                .tieneCalefaccion(request.getTieneCalefaccion())
                .tieneAireAcondicionado(request.getTieneAireAcondicionado())
                .build();
    }
}
