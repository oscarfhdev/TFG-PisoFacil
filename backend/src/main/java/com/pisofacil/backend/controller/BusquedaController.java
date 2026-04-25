package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.HabitacionConScoreDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.service.BusquedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/busqueda")
@RequiredArgsConstructor
public class BusquedaController {

    private final BusquedaService busquedaService;

    /**
     * GET /api/busqueda?idUsuario=X&ciudad=Y&precioMax=Z
     * Busca habitaciones compatibles con el perfil del usuario.
     * Devuelve los resultados con su puntuación de compatibilidad.
     */
    @GetMapping
    public ResponseEntity<List<HabitacionConScoreDTO>> buscarCompatibles(
            Authentication authentication,
            @RequestParam String ciudad,
            @RequestParam(required = false) BigDecimal precioMax) {

        List<HabitacionConScoreDTO> dtos = busquedaService
                .buscarHabitacionesCompatibles(authentication.getName(), ciudad, precioMax)
                .stream()
                .map(resultado -> {
                    Habitacion h = resultado.habitacion();
                    Piso p = h.getPiso();

                    return HabitacionConScoreDTO.builder()
                            .idHabitacion(h.getIdHabitacion())
                            .idPiso(p != null ? p.getIdPiso() : null)
                            .tituloAnuncio(h.getTituloAnuncio())
                            .precioMensual(h.getPrecioMensual())
                            .descripcionEspecifica(h.getDescripcionEspecifica())
                            .estaDisponible(h.getEstaDisponible())
                            .fechaPublicacion(h.getFechaPublicacion())
                            .superficieM2(h.getSuperficieM2())
                            .tieneBanoPrivado(h.getTieneBanoPrivado())
                            .amueblada(h.getAmueblada())
                            .exterior(h.getExterior())
                            .tieneCalefaccion(h.getTieneCalefaccion())
                            .tieneAireAcondicionado(h.getTieneAireAcondicionado())
                            .ciudad(p != null ? p.getCiudad() : null)
                            .direccion(p != null ? p.getDireccion() : null)
                            .codigoPostal(p != null ? p.getCodigoPostal() : null)
                            .compatibilidad(resultado.score())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
