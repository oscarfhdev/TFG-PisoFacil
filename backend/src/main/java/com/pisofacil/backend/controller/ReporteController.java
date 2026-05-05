package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.ReporteRequestDTO;
import com.pisofacil.backend.dto.ReporteResponseDTO;
import com.pisofacil.backend.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> findAll() {
        return ResponseEntity.ok(reporteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> create(@Valid @RequestBody ReporteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.create(dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReporteResponseDTO> updateEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(reporteService.updateEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reporteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
