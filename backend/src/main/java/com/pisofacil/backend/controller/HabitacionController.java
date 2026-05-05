package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> findAll() {
        return ResponseEntity.ok(habitacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.findById(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<HabitacionResponseDTO>> findDisponibles() {
        return ResponseEntity.ok(habitacionService.findDisponibles());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<HabitacionResponseDTO>> buscar(
            @RequestParam String ciudad,
            @RequestParam(required = false) BigDecimal precioMax) {
        return ResponseEntity.ok(habitacionService.findByCiudadAndPrecio(ciudad, precioMax));
    }

    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> create(@Valid @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.ok(habitacionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habitacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
