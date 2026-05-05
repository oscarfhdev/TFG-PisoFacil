package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.FavoritoRequestDTO;
import com.pisofacil.backend.dto.FavoritoResponseDTO;
import com.pisofacil.backend.mapper.FavoritoMapper;
import com.pisofacil.backend.model.Favorito;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.FavoritoRepository;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabitacionRepository habitacionRepository;
    private final FavoritoMapper favoritoMapper;

    @Transactional(readOnly = true)
    public List<FavoritoResponseDTO> findByUsuario(Long idUsuario) {
        return favoritoMapper.toResponseDTOList(favoritoRepository.findByUsuarioIdUsuario(idUsuario));
    }

    @Transactional(readOnly = true)
    public Boolean checkFavorito(Long idUsuario, Long idHabitacion) {
        return favoritoRepository.existsByUsuarioIdUsuarioAndHabitacionIdHabitacion(idUsuario, idHabitacion);
    }

    @Transactional
    public FavoritoResponseDTO create(FavoritoRequestDTO dto) {
        if (favoritoRepository.existsByUsuarioIdUsuarioAndHabitacionIdHabitacion(dto.getIdUsuario(), dto.getIdHabitacion())) {
            throw new IllegalArgumentException("El favorito ya existe");
        }

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        Habitacion habitacion = habitacionRepository.findById(dto.getIdHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + dto.getIdHabitacion()));

        Favorito favorito = favoritoMapper.toEntity(dto);
        favorito.setUsuario(usuario);
        favorito.setHabitacion(habitacion);

        return favoritoMapper.toResponseDTO(favoritoRepository.save(favorito));
    }

    @Transactional
    public void delete(Long id) {
        if (!favoritoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Favorito no encontrado con ID: " + id);
        }
        favoritoRepository.deleteById(id);
    }
}
