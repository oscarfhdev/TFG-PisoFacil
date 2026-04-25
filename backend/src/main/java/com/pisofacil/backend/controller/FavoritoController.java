package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.HabitacionDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            Authentication authentication,
            @RequestParam Long idHabitacion) {

        favoritoService.agregarFavorito(authentication.getName(), idHabitacion);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * DELETE /api/favoritos?idUsuario=X&idHabitacion=Y
     * Elimina una habitación de los favoritos del usuario.
     */
    @DeleteMapping
    public ResponseEntity<Void> eliminarFavorito(
            Authentication authentication,
            @RequestParam Long idHabitacion) {

        favoritoService.eliminarFavorito(authentication.getName(), idHabitacion);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/favoritos/{idUsuario}
     * Lista todas las habitaciones favoritas de un usuario.
     */
    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> listarFavoritos(Authentication authentication) {
        List<Habitacion> favoritos = favoritoService.listarFavoritosPorUsuario(authentication.getName());

        List<HabitacionDTO> dtos = favoritos.stream()
                .map(HabitacionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
