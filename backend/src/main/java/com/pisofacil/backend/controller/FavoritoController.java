package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.HabitacionDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    /**
     * POST /api/favoritos?idUsuario=X&idHabitacion=Y
     * Añade una habitación a los favoritos del usuario.
     */
    @PostMapping
    public ResponseEntity<Void> agregarFavorito(
            @RequestParam Long idUsuario,
            @RequestParam Long idHabitacion) {

        favoritoService.agregarFavorito(idUsuario, idHabitacion);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * DELETE /api/favoritos?idUsuario=X&idHabitacion=Y
     * Elimina una habitación de los favoritos del usuario.
     */
    @DeleteMapping
    public ResponseEntity<Void> eliminarFavorito(
            @RequestParam Long idUsuario,
            @RequestParam Long idHabitacion) {

        favoritoService.eliminarFavorito(idUsuario, idHabitacion);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/favoritos/{idUsuario}
     * Lista todas las habitaciones favoritas de un usuario.
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<HabitacionDTO>> listarFavoritos(@PathVariable Long idUsuario) {
        List<Habitacion> favoritos = favoritoService.listarFavoritosPorUsuario(idUsuario);

        List<HabitacionDTO> dtos = favoritos.stream()
                .map(HabitacionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
