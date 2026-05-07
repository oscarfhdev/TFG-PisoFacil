package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.UsuarioRequestDTO;
import com.pisofacil.backend.dto.UsuarioResponseDTO;
import com.pisofacil.backend.dto.PerfilUpdateRequestDTO;
import com.pisofacil.backend.dto.CambiarPasswordRequestDTO;
import com.pisofacil.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponseDTO> toggleEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.toggleEstadoCuenta(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> getMyProfile() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.findByEmail(email));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> updateMyProfile(@Valid @RequestBody PerfilUpdateRequestDTO dto) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioResponseDTO user = usuarioService.findByEmail(email);
        return ResponseEntity.ok(usuarioService.updatePerfil(user.getIdUsuario(), dto));
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changeMyPassword(@Valid @RequestBody CambiarPasswordRequestDTO dto) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioResponseDTO user = usuarioService.findByEmail(email);
        try {
            usuarioService.cambiarPassword(user.getIdUsuario(), dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/foto-perfil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponseDTO> uploadFotoPerfil(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        // Implementación temporal simple: solo retornamos el usuario existente
        // En un futuro se guardará en S3 o disco local
        return ResponseEntity.ok(usuarioService.findById(id));
    }
}
