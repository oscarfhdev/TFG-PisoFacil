package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.ReporteRequestDTO;
import com.pisofacil.backend.dto.ReporteResponseDTO;
import com.pisofacil.backend.mapper.ReporteMapper;
import com.pisofacil.backend.model.Reporte;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.ReporteRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReporteMapper reporteMapper;

    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> findAll() {
        return reporteMapper.toResponseDTOList(reporteRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ReporteResponseDTO findById(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + id));
        return reporteMapper.toResponseDTO(reporte);
    }

    @Transactional
    public ReporteResponseDTO create(ReporteRequestDTO dto) {
        Usuario emisor = usuarioRepository.findById(dto.getIdUsuarioEmisor())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuarioEmisor()));

        Reporte reporte = reporteMapper.toEntity(dto);
        reporte.setUsuarioEmisor(emisor);

        return reporteMapper.toResponseDTO(reporteRepository.save(reporte));
    }

    @Transactional
    public ReporteResponseDTO updateEstado(Long id, String estado) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + id));

        reporte.setEstado(estado);
        return reporteMapper.toResponseDTO(reporteRepository.save(reporte));
    }

    @Transactional
    public void delete(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reporte no encontrado con ID: " + id);
        }
        reporteRepository.deleteById(id);
    }
}
