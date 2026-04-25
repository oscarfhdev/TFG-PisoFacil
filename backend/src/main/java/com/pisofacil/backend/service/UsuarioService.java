package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.auth.LoginRequest;
import com.pisofacil.backend.dto.auth.LoginResponse;
import com.pisofacil.backend.dto.auth.RegisterRequest;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Registra un nuevo usuario con contraseña encriptada con BCrypt.
     */
    @Transactional
    public Usuario registrarUsuario(RegisterRequest request) {
        // Verificar que el email no esté duplicado
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.email());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // BCrypt real
                .fechaRegistro(LocalDateTime.now())
                .esAdmin(false)
                .esFumador(false)
                .tieneMascota(false)
                .tienePareja(false)
                .perfilLgtbi(false)
                .build();

        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica un usuario y devuelve un token JWT.
     */
    public LoginResponse login(LoginRequest request) {
        // 1. Buscar por email
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas"));

        // 2. Comparar contraseña (raw vs hash con BCrypt)
        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        // 3. Generar token JWT
        String token = jwtUtil.generateToken(usuario);

        return new LoginResponse(usuario.getEmail(), token, com.pisofacil.backend.mapper.UsuarioMapper.toDTO(usuario));
    }

    /**
     * Actualiza los datos del perfil de un usuario existente.
     * Permite actualizar datos personales y rasgos de compatibilidad.
     */
    @Transactional
    public Usuario actualizarPerfil(Long idUsuario, Usuario datosNuevos) {
        Usuario existente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con ID: " + idUsuario));

        // Actualizar datos personales
        if (datosNuevos.getNombre() != null) existente.setNombre(datosNuevos.getNombre());
        if (datosNuevos.getFechaNacimiento() != null) existente.setFechaNacimiento(datosNuevos.getFechaNacimiento());
        if (datosNuevos.getGenero() != null) existente.setGenero(datosNuevos.getGenero());
        if (datosNuevos.getEstudios() != null) existente.setEstudios(datosNuevos.getEstudios());
        if (datosNuevos.getBiografia() != null) existente.setBiografia(datosNuevos.getBiografia());
        if (datosNuevos.getFotoPerfilUrl() != null) existente.setFotoPerfilUrl(datosNuevos.getFotoPerfilUrl());
        if (datosNuevos.getInstagramUrl() != null) existente.setInstagramUrl(datosNuevos.getInstagramUrl());

        // Actualizar rasgos de compatibilidad
        if (datosNuevos.getEsFumador() != null) existente.setEsFumador(datosNuevos.getEsFumador());
        if (datosNuevos.getTieneMascota() != null) existente.setTieneMascota(datosNuevos.getTieneMascota());
        if (datosNuevos.getTienePareja() != null) existente.setTienePareja(datosNuevos.getTienePareja());
        if (datosNuevos.getPerfilLgtbi() != null) existente.setPerfilLgtbi(datosNuevos.getPerfilLgtbi());

        return usuarioRepository.save(existente);
    }

    /**
     * Obtiene un usuario por su ID.
     */
    public Usuario obtenerUsuarioPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con ID: " + idUsuario));
    }

    /**
     * Obtiene un usuario por su email (útil para el endpoint /me).
     */
    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con email: " + email));
    }

    /**
     * Cambia la contraseña de un usuario.
     * Verifica que la contraseña actual sea correcta antes de cambiarla.
     */
    @Transactional
    public void cambiarPassword(Long idUsuario, String passwordActual, String passwordNueva) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con ID: " + idUsuario));

        // Verificar que la contraseña actual es correcta
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // Encriptar y guardar la nueva contraseña
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
    }
}
