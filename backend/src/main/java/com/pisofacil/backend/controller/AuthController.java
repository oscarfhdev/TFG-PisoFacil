package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.auth.LoginRequest;
import com.pisofacil.backend.dto.auth.LoginResponse;
import com.pisofacil.backend.dto.auth.RegisterRequest;
import com.pisofacil.backend.dto.auth.RegisterResponse;
import com.pisofacil.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    /**
     * POST /api/auth/login
     * Autentica un usuario y devuelve un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    /**
     * POST /api/auth/registro
     * Registra un nuevo usuario (ruta pública).
     */
    @PostMapping("/registro")
    public ResponseEntity<RegisterResponse> registro(@Valid @RequestBody RegisterRequest request) {
        usuarioService.registrarUsuario(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse(request.email(), "Usuario registrado correctamente"));
    }
}
