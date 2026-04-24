package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.HabitacionDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.service.BusquedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * precioMax es opcional.
     */
    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> buscarCompatibles(
            @RequestParam Long idUsuario,
            @RequestParam String ciudad,
            @RequestParam(required = false) BigDecimal precioMax) {

        List<Habitacion> resultados = busquedaService
                .buscarHabitacionesCompatibles(idUsuario, ciudad, precioMax);

        List<HabitacionDTO> dtos = resultados.stream()
                .map(HabitacionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
