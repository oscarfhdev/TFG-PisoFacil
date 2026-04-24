package com.pisofacil.backend.service;

import com.pisofacil.backend.model.Favorito;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.FavoritoRepository;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabitacionRepository habitacionRepository;

    /**
     * Añade una habitación a los favoritos de un usuario.
     * Verifica que ambos existan y que no haya un duplicado.
     */
    @Transactional
    public Favorito agregarFavorito(Long idUsuario, Long idHabitacion) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con ID: " + idUsuario));

        // Verificar que la habitación existe
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la habitación con ID: " + idHabitacion));

        // Verificar que no exista ya ese favorito (UniqueConstraint en la tabla)
        if (favoritoRepository.existsByUsuarioIdUsuarioAndHabitacionIdHabitacion(idUsuario, idHabitacion)) {
            throw new IllegalArgumentException(
                    "La habitación ya está en favoritos del usuario");
        }

        Favorito favorito = Favorito.builder()
                .usuario(usuario)
                .habitacion(habitacion)
                .build();

        return favoritoRepository.save(favorito);
    }

    /**
     * Elimina una habitación de los favoritos de un usuario.
     */
    @Transactional
    public void eliminarFavorito(Long idUsuario, Long idHabitacion) {
        Favorito favorito = favoritoRepository
                .findByUsuarioIdUsuarioAndHabitacionIdHabitacion(idUsuario, idHabitacion)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el favorito para usuario " + idUsuario
                                + " y habitación " + idHabitacion));

        favoritoRepository.delete(favorito);
    }

    /**
     * Lista todas las habitaciones favoritas de un usuario.
     * Devuelve las entidades Habitacion directamente.
     */
    public List<Habitacion> listarFavoritosPorUsuario(Long idUsuario) {
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new EntityNotFoundException(
                    "No se encontró el usuario con ID: " + idUsuario);
        }

        return favoritoRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(Favorito::getHabitacion)
                .collect(Collectors.toList());
    }
}
