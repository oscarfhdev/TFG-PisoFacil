package com.pisofacil.backend.service;

import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusquedaService {

    private final UsuarioRepository usuarioRepository;
    private final HabitacionRepository habitacionRepository;

    /**
     * Resultado de búsqueda que incluye la habitación y su score de compatibilidad.
     */
    public record ResultadoBusqueda(Habitacion habitacion, int score) {}

    /**
     * Busca habitaciones compatibles con el perfil del usuario.
     * Devuelve ResultadoBusqueda con el score incluido para que el controller pueda
     * enviarlo al frontend.
     *
     * @param idUsuarioQueBusca ID del usuario que busca habitación
     * @param ciudad            Ciudad donde buscar
     * @param precioMaximo      Precio máximo mensual (puede ser null)
     * @return Lista de ResultadoBusqueda ordenada de mayor a menor compatibilidad
     */
    public List<ResultadoBusqueda> buscarHabitacionesCompatibles(String emailUsuarioQueBusca, String ciudad, BigDecimal precioMaximo) {
        // PASO 1: Obtener el perfil del usuario que busca
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioQueBusca)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con email: " + emailUsuarioQueBusca));
        Long idUsuarioQueBusca = usuario.getIdUsuario();

        // PASO 2: Filtrar habitaciones disponibles por ciudad y precio máximo
        List<Habitacion> habitacionesFiltradas;

        if (precioMaximo != null) {
            habitacionesFiltradas = habitacionRepository
                    .findByEstaDisponibleTrueAndPisoCiudadIgnoreCaseAndPrecioMensualLessThanEqual(
                            ciudad, precioMaximo);
        } else {
            habitacionesFiltradas = habitacionRepository
                    .findByEstaDisponibleTrueAndPisoCiudadIgnoreCase(ciudad);
        }

        // Excluir las habitaciones del propio usuario (no se busca a sí mismo)
        habitacionesFiltradas = habitacionesFiltradas.stream()
                .filter(h -> !h.getPiso().getUsuario().getIdUsuario().equals(idUsuarioQueBusca))
                .collect(Collectors.toList());

        // PASO 3: Calcular score y crear resultados
        List<ResultadoBusqueda> resultados = habitacionesFiltradas.stream()
                .map(h -> new ResultadoBusqueda(h, calcularScore(usuario, h.getPiso())))
                .sorted(Comparator.comparingInt(ResultadoBusqueda::score).reversed())
                .collect(Collectors.toList());

        return resultados;
    }

    /**
     * Calcula la puntuación de compatibilidad entre un usuario y un piso.
     * Sistema de "Atributos Espejo": compara los rasgos del usuario con las
     * normas/preferencias del piso.
     *
     * Puntuación base: 100 puntos.
     * - Cada incompatibilidad CRÍTICA (descalificadora): -100 puntos
     * - Cada compatibilidad positiva: +10 puntos
     *
     * @return Puntuación de compatibilidad (mayor = más compatible)
     */
    private int calcularScore(Usuario usuario, Piso piso) {
        int score = 100;

        // ── Incompatibilidades CRÍTICAS (descalificadoras) ──

        // Tabaco: si el piso NO admite fumadores y el usuario ES fumador → descartado
        if (Boolean.FALSE.equals(piso.getAdmiteFumadores()) && Boolean.TRUE.equals(usuario.getEsFumador())) {
            score -= 100;
        }

        // Mascotas: si el piso NO admite mascotas y el usuario TIENE mascota → descartado
        if (Boolean.FALSE.equals(piso.getAdmiteMascotas()) && Boolean.TRUE.equals(usuario.getTieneMascota())) {
            score -= 100;
        }

        // Parejas: si el piso NO admite parejas y el usuario TIENE pareja → descartado
        if (Boolean.FALSE.equals(piso.getAdmiteParejas()) && Boolean.TRUE.equals(usuario.getTienePareja())) {
            score -= 100;
        }

        // LGTBI: si el piso NO es LGTBI-friendly y el usuario tiene perfil LGTBI → descartado
        if (Boolean.FALSE.equals(piso.getLgtbiFriendly()) && Boolean.TRUE.equals(usuario.getPerfilLgtbi())) {
            score -= 100;
        }

        // ── Compatibilidades POSITIVAS (bonificaciones) ──

        // El piso admite fumadores y el usuario no fuma → buen ambiente para ambos
        if (Boolean.TRUE.equals(piso.getAdmiteFumadores()) && Boolean.FALSE.equals(usuario.getEsFumador())) {
            score += 5;
        }

        // El piso es LGTBI-friendly → bonus general de inclusividad
        if (Boolean.TRUE.equals(piso.getLgtbiFriendly())) {
            score += 10;
        }

        // Servicios extra del piso suman puntos
        if (Boolean.TRUE.equals(piso.getTieneWifi())) score += 5;
        if (Boolean.TRUE.equals(piso.getTieneAscensor())) score += 5;

        return score;
    }
}
