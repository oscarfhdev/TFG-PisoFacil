package com.pisofacil.backend.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsResponseDTO {

    // Totales globales
    private long totalUsuarios;
    private long totalPisos;
    private long totalHabitaciones;
    private long totalReportes;

    // Habitaciones por disponibilidad
    private long habitacionesDisponibles;
    private long habitacionesOcupadas;

    // Reportes por estado
    private long reportesPendientes;
    private long reportesResueltos;
    private long reportesRechazados;

    // Usuarios activos / suspendidos
    private long usuariosActivos;
    private long usuariosSuspendidos;

    // Distribución geográfica de pisos por ciudad
    private Map<String, Long> pisosPorCiudad;

    // Distribución de habitaciones por rango de precio
    private Map<String, Long> habitacionesPorRangoPrecio;

    // Top 5 usuarios con más pisos publicados
    private List<UsuarioConPisosDTO> topPublicadores;
}
