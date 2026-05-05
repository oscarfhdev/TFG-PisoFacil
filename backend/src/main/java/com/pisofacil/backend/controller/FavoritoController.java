package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.FavoritoRequestDTO;
import com.pisofacil.backend.dto.FavoritoResponseDTO;
import com.pisofacil.backend.service.FavoritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<FavoritoResponseDTO>> findByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(favoritoService.findByUsuario(idUsuario));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorito(
            @RequestParam Long idUsuario,
            @RequestParam Long idHabitacion) {
        return ResponseEntity.ok(favoritoService.checkFavorito(idUsuario, idHabitacion));
    }

    @PostMapping
    public ResponseEntity<FavoritoResponseDTO> create(@Valid @RequestBody FavoritoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoritoService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        favoritoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
