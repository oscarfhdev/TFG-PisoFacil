package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.CambioPasswordRequest;
import com.pisofacil.backend.dto.UsuarioDTO;
import com.pisofacil.backend.mapper.UsuarioMapper;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * GET /api/usuarios/me
     * Obtiene el perfil del usuario autenticado (sacado del JWT).
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> obtenerPerfilActual(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
    }

    /**
     * GET /api/usuarios/{id}
     * Obtiene el perfil público de un usuario.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza el perfil de un usuario (datos personales + rasgos de compatibilidad).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarPerfil(@PathVariable Long id, @RequestBody UsuarioDTO datosNuevos) {
        Usuario entidadDatos = UsuarioMapper.toEntity(datosNuevos);
        Usuario actualizado = usuarioService.actualizarPerfil(id, entidadDatos);
        return ResponseEntity.ok(UsuarioMapper.toDTO(actualizado));
    }

    /**
     * PUT /api/usuarios/{id}/password
     * Cambia la contraseña del usuario (requiere contraseña actual).
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable Long id,
            @RequestBody CambioPasswordRequest request) {

        usuarioService.cambiarPassword(id, request.getPasswordActual(), request.getPasswordNueva());
        return ResponseEntity.ok().build();
    }
}
