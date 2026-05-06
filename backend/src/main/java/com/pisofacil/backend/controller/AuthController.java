package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.LoginRequestDTO;
import com.pisofacil.backend.dto.LoginResponseDTO;
import com.pisofacil.backend.dto.RegisterRequestDTO;
import com.pisofacil.backend.dto.RegisterResponseDTO;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya está en uso");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setApellidos(request.getApellidos());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setFechaNacimiento(request.getFechaNacimiento());
        nuevoUsuario.setEstudios(request.getEstudios());
        nuevoUsuario.setBiografia(request.getBiografia());
        nuevoUsuario.setEsFumador(request.getEsFumador());
        nuevoUsuario.setTieneMascota(request.getTieneMascota());
        nuevoUsuario.setTienePareja(request.getTienePareja());
        nuevoUsuario.setPerfilLgtbi(request.getPerfilLgtbi());
        // Por defecto, no es admin (ya se maneja en @PrePersist de Usuario)

        Usuario guardado = usuarioRepository.save(nuevoUsuario);

        RegisterResponseDTO response = new RegisterResponseDTO(
                guardado.getIdUsuario(),
                guardado.getNombre(),
                guardado.getEmail(),
                "Usuario registrado exitosamente");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.email());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(usuario);
        String role = Boolean.TRUE.equals(usuario.getEsAdmin()) ? "ADMIN" : "USER";

        LoginResponseDTO response = new LoginResponseDTO(
                token,
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                role);

        return ResponseEntity.ok(response);
    }
}
