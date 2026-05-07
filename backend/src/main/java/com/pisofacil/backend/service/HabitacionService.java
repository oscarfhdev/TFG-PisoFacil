package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.HabitacionSpecification;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionMapper habitacionMapper;

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> findDestacadas() {
        return habitacionMapper.toResponseDTOList(habitacionRepository.findDestacadasRandom());
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> findAll() {
        return habitacionMapper.toResponseDTOList(habitacionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public HabitacionResponseDTO findById(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));
        return habitacionMapper.toResponseDTO(habitacion);
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> findDisponibles() {
        return habitacionMapper.toResponseDTOList(habitacionRepository.findByEstaDisponibleTrue());
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> buscarAvanzado(
            String ciudad, BigDecimal precioMin, BigDecimal precioMax,
            Boolean tieneBanoPrivado, Boolean exterior,
            Boolean tieneAireAcondicionado, Boolean admiteMascotas,
            Boolean admiteFumadores, Boolean lgtbiFriendly) {

        Specification<Habitacion> spec = HabitacionSpecification.buildFiltro(
            ciudad, precioMin, precioMax, tieneBanoPrivado, exterior,
            tieneAireAcondicionado, admiteMascotas, admiteFumadores, lgtbiFriendly);

        return habitacionMapper.toResponseDTOList(habitacionRepository.findAll(spec));
    }

    @Transactional
    public HabitacionResponseDTO create(HabitacionRequestDTO dto) {
        Piso piso = pisoRepository.findById(dto.getIdPiso())
                .orElseThrow(() -> new ResourceNotFoundException("Piso no encontrado con ID: " + dto.getIdPiso()));

        Habitacion habitacion = habitacionMapper.toEntity(dto);
        habitacion.setPiso(piso);

        return habitacionMapper.toResponseDTO(habitacionRepository.save(habitacion));
    }

    @Transactional
    public HabitacionResponseDTO update(Long id, HabitacionRequestDTO dto) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));

        Piso piso = pisoRepository.findById(dto.getIdPiso())
                .orElseThrow(() -> new ResourceNotFoundException("Piso no encontrado con ID: " + dto.getIdPiso()));

        habitacion.setPiso(piso);
        habitacion.setTituloAnuncio(dto.getTituloAnuncio());
        habitacion.setPrecioMensual(dto.getPrecioMensual());
        habitacion.setDescripcionEspecifica(dto.getDescripcionEspecifica());
        if (dto.getEstaDisponible() != null) {
            habitacion.setEstaDisponible(dto.getEstaDisponible());
        }
        habitacion.setSuperficieM2(dto.getSuperficieM2());
        habitacion.setTieneBanoPrivado(dto.getTieneBanoPrivado());
        habitacion.setAmueblada(dto.getAmueblada());
        habitacion.setExterior(dto.getExterior());
        habitacion.setTieneCalefaccion(dto.getTieneCalefaccion());
        habitacion.setTieneAireAcondicionado(dto.getTieneAireAcondicionado());

        return habitacionMapper.toResponseDTO(habitacionRepository.save(habitacion));
    }

    @Transactional
    public void delete(Long id) {
        if (!habitacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Habitación no encontrada con ID: " + id);
        }
        habitacionRepository.deleteById(id);
    }
}
