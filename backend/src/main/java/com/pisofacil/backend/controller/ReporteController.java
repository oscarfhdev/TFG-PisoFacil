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

import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> findAll() {
        return ResponseEntity.ok(reporteService.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> create(@Valid @RequestBody ReporteRequestDTO dto) {
        Usuario usuario = getUsuarioAutenticado();
        // Generar título automáticamente basado en la categoría (simplifica UX)
        dto.setTitulo(dto.getCategoria());
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.create(dto, usuario));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReporteResponseDTO> updateEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(reporteService.updateEstado(id, estado));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reporteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
