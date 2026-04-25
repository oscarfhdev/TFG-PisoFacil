package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.AnuncioUpdateRequest;
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
    public Habitacion publicarAnuncio(String emailUsuario, Piso piso, Habitacion habitacion, List<Foto> fotos) {
        // 1. Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con email: " + emailUsuario));

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
                if (foto.getHabitacion() != null) {
                    foto.setHabitacion(habitacionGuardada);
                }
            }
            fotoRepository.saveAll(fotos);
        }

        return habitacionGuardada;
    }

    /**
     * Obtiene los datos completos de una habitación por su ID.
     */
    public Habitacion obtenerHabitacionPorId(Long idHabitacion) {
        return habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la habitación con ID: " + idHabitacion));
    }

    /**
     * Lista todas las habitaciones publicadas por un usuario (sus anuncios).
     */
    public List<Habitacion> listarAnunciosPorUsuario(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con email: " + emailUsuario));
        Long idUsuario = usuario.getIdUsuario();

        // Buscar los pisos del usuario y extraer sus habitaciones
        List<Piso> pisos = pisoRepository.findByUsuarioIdUsuario(idUsuario);
        return pisos.stream()
                .flatMap(piso -> habitacionRepository.findByPisoIdPiso(piso.getIdPiso()).stream())
                .toList();
    }

    /**
     * Lista todas las habitaciones disponibles en el sistema.
     */
    public List<Habitacion> listarHabitacionesDisponibles() {
        return habitacionRepository.findByEstaDisponibleTrue();
    }

    /**
     * Actualiza un anuncio existente (habitación + datos del piso).
     */
    @Transactional
    public Habitacion editarAnuncio(Long idHabitacion, AnuncioUpdateRequest request) {
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la habitación con ID: " + idHabitacion));

        Piso piso = habitacion.getPiso();

        // Actualizar campos de la habitación
        if (request.getTituloAnuncio() != null) habitacion.setTituloAnuncio(request.getTituloAnuncio());
        if (request.getPrecioMensual() != null) habitacion.setPrecioMensual(request.getPrecioMensual());
        if (request.getDescripcionEspecifica() != null) habitacion.setDescripcionEspecifica(request.getDescripcionEspecifica());
        if (request.getSuperficieM2() != null) habitacion.setSuperficieM2(request.getSuperficieM2());
        if (request.getTieneBanoPrivado() != null) habitacion.setTieneBanoPrivado(request.getTieneBanoPrivado());
        if (request.getAmueblada() != null) habitacion.setAmueblada(request.getAmueblada());
        if (request.getExterior() != null) habitacion.setExterior(request.getExterior());
        if (request.getTieneCalefaccion() != null) habitacion.setTieneCalefaccion(request.getTieneCalefaccion());
        if (request.getTieneAireAcondicionado() != null) habitacion.setTieneAireAcondicionado(request.getTieneAireAcondicionado());

        // Actualizar campos del piso
        if (request.getDescripcionGlobal() != null) piso.setDescripcionGlobal(request.getDescripcionGlobal());
        if (request.getTieneWifi() != null) piso.setTieneWifi(request.getTieneWifi());
        if (request.getTieneAscensor() != null) piso.setTieneAscensor(request.getTieneAscensor());
        if (request.getAdmiteFumadores() != null) piso.setAdmiteFumadores(request.getAdmiteFumadores());
        if (request.getAdmiteMascotas() != null) piso.setAdmiteMascotas(request.getAdmiteMascotas());
        if (request.getAdmiteParejas() != null) piso.setAdmiteParejas(request.getAdmiteParejas());
        if (request.getLgtbiFriendly() != null) piso.setLgtbiFriendly(request.getLgtbiFriendly());

        pisoRepository.save(piso);
        return habitacionRepository.save(habitacion);
    }

    /**
     * Cambia la disponibilidad de una habitación (disponible/no disponible).
     */
    @Transactional
    public Habitacion cambiarDisponibilidad(Long idHabitacion, boolean disponible) {
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la habitación con ID: " + idHabitacion));

        habitacion.setEstaDisponible(disponible);
        return habitacionRepository.save(habitacion);
    }

    /**
     * Elimina un anuncio (habitación) y gestiona sus dependencias:
     * primero elimina los favoritos asociados, luego la habitación.
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

    /**
     * Obtiene las fotos de un piso.
     */
    public List<Foto> obtenerFotosPorPiso(Long idPiso) {
        if (!pisoRepository.existsById(idPiso)) {
            throw new EntityNotFoundException("No se encontró el piso con ID: " + idPiso);
        }
        return fotoRepository.findByPisoIdPiso(idPiso);
    }

    /**
     * Obtiene las fotos de una habitación.
     */
    public List<Foto> obtenerFotosPorHabitacion(Long idHabitacion) {
        if (!habitacionRepository.existsById(idHabitacion)) {
            throw new EntityNotFoundException("No se encontró la habitación con ID: " + idHabitacion);
        }
        return fotoRepository.findByHabitacionIdHabitacion(idHabitacion);
    }
}
