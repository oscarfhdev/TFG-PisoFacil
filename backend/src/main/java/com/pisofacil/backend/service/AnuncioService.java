package com.pisofacil.backend.service;

import com.pisofacil.backend.model.Foto;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnuncioService {

    private final UsuarioRepository usuarioRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionRepository habitacionRepository;
    private final FotoRepository fotoRepository;
    private final FavoritoRepository favoritoRepository;

    /**
     * Publica un anuncio completo: crea el Piso, la Habitación asociada y las Fotos.
     * Todo en una sola transacción para garantizar consistencia.
     */
    @Transactional
    public Habitacion publicarAnuncio(Long idUsuario, Piso piso, Habitacion habitacion, List<Foto> fotos) {
        // 1. Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con ID: " + idUsuario));

        // 2. Asociar usuario al piso y guardarlo
        piso.setUsuario(usuario);
        Piso pisoGuardado = pisoRepository.save(piso);

        // 3. Asociar piso a la habitación y guardarla
        habitacion.setPiso(pisoGuardado);
        if (habitacion.getEstaDisponible() == null) {
            habitacion.setEstaDisponible(true);
        }
        Habitacion habitacionGuardada = habitacionRepository.save(habitacion);

        // 4. Asociar piso (y opcionalmente habitación) a cada foto y guardarlas
        if (fotos != null && !fotos.isEmpty()) {
            for (Foto foto : fotos) {
                foto.setPiso(pisoGuardado);
                // Si la foto es específica de la habitación, la asociamos también
                if (foto.getHabitacion() != null) {
                    foto.setHabitacion(habitacionGuardada);
                }
            }
            fotoRepository.saveAll(fotos);
        }

        return habitacionGuardada;
    }

    /**
     * Lista todas las habitaciones disponibles en el sistema.
     */
    public List<Habitacion> listarHabitacionesDisponibles() {
        return habitacionRepository.findByEstaDisponibleTrue();
    }

    /**
     * Elimina un anuncio (habitación) y gestiona sus dependencias:
     * primero elimina los favoritos asociados, luego la habitación
     * (las fotos se eliminan por cascade).
     */
    @Transactional
    public void eliminarAnuncio(Long idHabitacion) {
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la habitación con ID: " + idHabitacion));

        // Eliminar favoritos asociados a esta habitación
        favoritoRepository.deleteByHabitacionIdHabitacion(idHabitacion);

        // Eliminar la habitación (las fotos asociadas se eliminan por cascade + orphanRemoval)
        habitacionRepository.delete(habitacion);
    }
}
