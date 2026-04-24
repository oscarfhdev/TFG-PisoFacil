package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.UsuarioDTO;
import com.pisofacil.backend.mapper.UsuarioMapper;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * POST /api/usuarios/registro
     * Registra un nuevo usuario en el sistema.
     */
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody Usuario usuario) {
        Usuario registrado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(UsuarioMapper.toDTO(registrado), HttpStatus.CREATED);
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
}
