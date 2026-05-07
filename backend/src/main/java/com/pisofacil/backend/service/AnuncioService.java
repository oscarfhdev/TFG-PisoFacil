package com.pisofacil.backend.service;

import com.pisofacil.backend.mapper.AnuncioMapper;
import com.pisofacil.backend.dto.PublicarAnuncioRequestDTO;
import com.pisofacil.backend.dto.PublicarAnuncioResponseDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.mapper.PisoMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.PisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnuncioService {

    private final PisoRepository pisoRepository;
    private final HabitacionRepository habitacionRepository;
    private final AnuncioMapper anuncioMapper;
    private final PisoMapper pisoMapper;
    private final HabitacionMapper habitacionMapper;

    @Transactional
    public PublicarAnuncioResponseDTO publicar(PublicarAnuncioRequestDTO dto, Usuario usuario) {
        // 1. Crear y guardar el Piso mapeando con AnuncioMapper
        Piso piso = anuncioMapper.toPiso(dto);
        piso.setUsuario(usuario);
        
        Piso pisoGuardado = pisoRepository.save(piso);

        // 2. Crear y guardar la primera Habitación vinculada al Piso
        Habitacion habitacion = anuncioMapper.toHabitacion(dto);
        habitacion.setPiso(pisoGuardado);
        
        Habitacion habitacionGuardada = habitacionRepository.save(habitacion);

        // 3. Construir y devolver la respuesta
        return PublicarAnuncioResponseDTO.builder()
                .mensaje("Anuncio publicado correctamente")
                .piso(pisoMapper.toResponseDTO(pisoGuardado))
                .habitacion(habitacionMapper.toResponseDTO(habitacionGuardada))
                .build();
    }
}
