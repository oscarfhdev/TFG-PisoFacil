package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.AdminStatsResponseDTO;
import com.pisofacil.backend.dto.UsuarioConPisosDTO;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.repository.ReporteRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionRepository habitacionRepository;
    private final ReporteRepository reporteRepository;

    @Transactional(readOnly = true)
    public AdminStatsResponseDTO getStats() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Piso> pisos = pisoRepository.findAll();
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        // Totales globales
        long totalUsuarios = usuarios.size();
        long totalPisos = pisos.size();
        long totalHabitaciones = habitaciones.size();
        long totalReportes = reporteRepository.count();

        // Habitaciones por disponibilidad
        long habitacionesDisponibles = habitaciones.stream().filter(h -> Boolean.TRUE.equals(h.getEstaDisponible())).count();
        long habitacionesOcupadas = totalHabitaciones - habitacionesDisponibles;

        // Reportes por estado
        long reportesPendientes = reporteRepository.findByEstado("PENDIENTE").size();
        long reportesResueltos = reporteRepository.findByEstado("RESUELTO").size();
        long reportesRechazados = reporteRepository.findByEstado("RECHAZADO").size();

        // Usuarios activos / suspendidos
        long usuariosActivos = usuarios.stream().filter(u -> Boolean.TRUE.equals(u.getCuentaActiva())).count();
        long usuariosSuspendidos = totalUsuarios - usuariosActivos;

        // Distribución de pisos por ciudad (top 8 ciudades)
        Map<String, Long> pisosPorCiudad = pisos.stream()
                .filter(p -> p.getCiudad() != null && !p.getCiudad().isBlank())
                .collect(Collectors.groupingBy(p -> p.getCiudad().trim(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(8)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Distribución de habitaciones por rango de precio
        Map<String, Long> habitacionesPorRangoPrecio = new LinkedHashMap<>();
        habitacionesPorRangoPrecio.put("< 300€", habitaciones.stream()
                .filter(h -> h.getPrecioMensual() != null && h.getPrecioMensual().compareTo(new BigDecimal("300")) < 0).count());
        habitacionesPorRangoPrecio.put("300-500€", habitaciones.stream()
                .filter(h -> h.getPrecioMensual() != null
                        && h.getPrecioMensual().compareTo(new BigDecimal("300")) >= 0
                        && h.getPrecioMensual().compareTo(new BigDecimal("500")) <= 0).count());
        habitacionesPorRangoPrecio.put("501-700€", habitaciones.stream()
                .filter(h -> h.getPrecioMensual() != null
                        && h.getPrecioMensual().compareTo(new BigDecimal("500")) > 0
                        && h.getPrecioMensual().compareTo(new BigDecimal("700")) <= 0).count());
        habitacionesPorRangoPrecio.put("> 700€", habitaciones.stream()
                .filter(h -> h.getPrecioMensual() != null && h.getPrecioMensual().compareTo(new BigDecimal("700")) > 0).count());

        // Top 5 publicadores
        Map<Usuario, Long> pisosCountByUser = pisos.stream()
                .collect(Collectors.groupingBy(Piso::getUsuario, Collectors.counting()));

        List<UsuarioConPisosDTO> topPublicadores = pisosCountByUser.entrySet().stream()
                .sorted(Map.Entry.<Usuario, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> UsuarioConPisosDTO.builder()
                        .idUsuario(entry.getKey().getIdUsuario())
                        .nombre(entry.getKey().getNombre())
                        .apellidos(entry.getKey().getApellidos())
                        .email(entry.getKey().getEmail())
                        .fotoPerfilUrl(entry.getKey().getFotoPerfilUrl())
                        .cantidadPisos(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return AdminStatsResponseDTO.builder()
                .totalUsuarios(totalUsuarios)
                .totalPisos(totalPisos)
                .totalHabitaciones(totalHabitaciones)
                .totalReportes(totalReportes)
                .habitacionesDisponibles(habitacionesDisponibles)
                .habitacionesOcupadas(habitacionesOcupadas)
                .reportesPendientes(reportesPendientes)
                .reportesResueltos(reportesResueltos)
                .reportesRechazados(reportesRechazados)
                .usuariosActivos(usuariosActivos)
                .usuariosSuspendidos(usuariosSuspendidos)
                .pisosPorCiudad(pisosPorCiudad)
                .habitacionesPorRangoPrecio(habitacionesPorRangoPrecio)
                .topPublicadores(topPublicadores)
                .build();
    }
}
