package com.pisofacil.backend.service;

import com.pisofacil.backend.model.Reporte;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.ReporteRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea un nuevo reporte en el sistema.
     * Asigna el usuario emisor, establece estado inicial "ABIERTO" y la fecha.
     */
    @Transactional
    public Reporte crearReporte(Long idUsuario, Reporte reporte) {
        // Verificar que el usuario emisor existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el usuario con ID: " + idUsuario));

        reporte.setUsuarioEmisor(usuario);
        reporte.setEstado("ABIERTO");
        reporte.setFechaCreacion(LocalDateTime.now());

        return reporteRepository.save(reporte);
    }

    /**
     * Lista todos los reportes abiertos (pendientes de resolución).
     * Pensado para la vista del administrador.
     */
    public List<Reporte> listarReportesAbiertos() {
        return reporteRepository.findByEstado("ABIERTO");
    }

    /**
     * Cierra/resuelve un reporte cambiando su estado.
     */
    @Transactional
    public Reporte cerrarReporte(Long idReporte) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el reporte con ID: " + idReporte));

        reporte.setEstado("RESUELTO");
        return reporteRepository.save(reporte);
    }
}
