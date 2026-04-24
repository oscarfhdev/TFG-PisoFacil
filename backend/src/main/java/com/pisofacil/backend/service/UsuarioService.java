package com.pisofacil.backend.service;

import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Registra un nuevo usuario en el sistema.
     * Verifica que el email no esté ya registrado.
     * TODO: Integrar encriptación real con BCrypt cuando se configure Spring Security.
     */
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        // Verificar que el email no esté duplicado
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        // Simulación de encriptación de contraseña (se reemplazará por BCrypt)
        usuario.setPassword("{noop}" + usuario.getPassword());

        // Establecer fecha de registro si no viene definida
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }

        // Establecer valores por defecto para booleanos nulos
        if (usuario.getEsAdmin() == null) usuario.setEsAdmin(false);
        if (usuario.getEsFumador() == null) usuario.setEsFumador(false);
        if (usuario.getTieneMascota() == null) usuario.setTieneMascota(false);
        if (usuario.getTienePareja() == null) usuario.setTienePareja(false);
        if (usuario.getPerfilLgtbi() == null) usuario.setPerfilLgtbi(false);

        return usuarioRepository.save(usuario);
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
}
