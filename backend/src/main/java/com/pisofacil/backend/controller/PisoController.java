package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.PisoRequestDTO;
import com.pisofacil.backend.dto.PisoResponseDTO;
import com.pisofacil.backend.dto.MisPisosResponseDTO;
import com.pisofacil.backend.service.PisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pisos")
@RequiredArgsConstructor
public class PisoController {

    private final PisoService pisoService;

    @GetMapping
    public ResponseEntity<List<PisoResponseDTO>> findAll() {
        return ResponseEntity.ok(pisoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PisoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pisoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PisoResponseDTO> create(@Valid @RequestBody PisoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pisoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PisoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PisoRequestDTO dto) {
        return ResponseEntity.ok(pisoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pisoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<MisPisosResponseDTO>> getMyPisos() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(pisoService.findByUsuarioEmail(email));
    }
}
