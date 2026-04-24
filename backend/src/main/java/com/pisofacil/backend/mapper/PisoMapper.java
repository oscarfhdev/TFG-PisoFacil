package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.AnuncioRequest;
import com.pisofacil.backend.dto.PisoDTO;
import com.pisofacil.backend.model.Piso;

public class PisoMapper {

    private PisoMapper() {
    }

    public static PisoDTO toDTO(Piso piso) {
        if (piso == null) return null;

        return PisoDTO.builder()
                .idPiso(piso.getIdPiso())
                .idUsuario(piso.getUsuario() != null ? piso.getUsuario().getIdUsuario() : null)
                .direccion(piso.getDireccion())
                .ciudad(piso.getCiudad())
                .codigoPostal(piso.getCodigoPostal())
                .numHabitacionesTotal(piso.getNumHabitacionesTotal())
                .numBanos(piso.getNumBanos())
                .planta(piso.getPlanta())
                .superficieTotalM2(piso.getSuperficieTotalM2())
                .tieneWifi(piso.getTieneWifi())
                .tieneAscensor(piso.getTieneAscensor())
                .descripcionGlobal(piso.getDescripcionGlobal())
                .admiteFumadores(piso.getAdmiteFumadores())
                .admiteMascotas(piso.getAdmiteMascotas())
                .admiteParejas(piso.getAdmiteParejas())
                .lgtbiFriendly(piso.getLgtbiFriendly())
                .fechaCreacion(piso.getFechaCreacion())
                .build();
    }

    /**
     * Extrae los campos del piso desde un AnuncioRequest.
     */
    public static Piso fromAnuncioRequest(AnuncioRequest request) {
        if (request == null) return null;

        return Piso.builder()
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .codigoPostal(request.getCodigoPostal())
                .numHabitacionesTotal(request.getNumHabitacionesTotal())
                .numBanos(request.getNumBanos())
                .planta(request.getPlanta())
                .superficieTotalM2(request.getSuperficieTotalM2())
                .tieneWifi(request.getTieneWifi())
                .tieneAscensor(request.getTieneAscensor())
                .descripcionGlobal(request.getDescripcionGlobal())
                .admiteFumadores(request.getAdmiteFumadores())
                .admiteMascotas(request.getAdmiteMascotas())
                .admiteParejas(request.getAdmiteParejas())
                .lgtbiFriendly(request.getLgtbiFriendly())
                .build();
    }
}
