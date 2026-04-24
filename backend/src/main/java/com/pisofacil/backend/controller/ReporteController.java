package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.ReporteDTO;
import com.pisofacil.backend.model.Reporte;
import com.pisofacil.backend.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * POST /api/reportes?idUsuario=X
     * Crea un nuevo reporte.
     */
    @PostMapping
    public ResponseEntity<ReporteDTO> crearReporte(
            @RequestParam Long idUsuario,
            @RequestBody Reporte reporte) {

        Reporte creado = reporteService.crearReporte(idUsuario, reporte);
        return new ResponseEntity<>(toDTO(creado), HttpStatus.CREATED);
    }

    /**
     * GET /api/reportes/abiertos
     * Lista todos los reportes abiertos (vista administrador).
     */
    @GetMapping("/abiertos")
    public ResponseEntity<List<ReporteDTO>> listarAbiertos() {
        List<ReporteDTO> reportes = reporteService.listarReportesAbiertos()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reportes);
    }

    /**
     * PUT /api/reportes/{id}/cerrar
     * Cierra/resuelve un reporte.
     */
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<ReporteDTO> cerrarReporte(@PathVariable Long id) {
        Reporte cerrado = reporteService.cerrarReporte(id);
        return ResponseEntity.ok(toDTO(cerrado));
    }

    /**
     * Conversión interna Reporte → ReporteDTO.
     * No se creó un ReporteMapper separado porque solo tiene un método.
     */
    private ReporteDTO toDTO(Reporte reporte) {
        return ReporteDTO.builder()
                .idReporte(reporte.getIdReporte())
                .idUsuarioEmisor(reporte.getUsuarioEmisor() != null
                        ? reporte.getUsuarioEmisor().getIdUsuario() : null)
                .categoria(reporte.getCategoria())
                .titulo(reporte.getTitulo())
                .mensaje(reporte.getMensaje())
                .fechaCreacion(reporte.getFechaCreacion())
                .estado(reporte.getEstado())
                .build();
    }
}
