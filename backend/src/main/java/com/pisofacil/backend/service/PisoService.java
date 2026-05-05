package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.PisoRequestDTO;
import com.pisofacil.backend.dto.PisoResponseDTO;
import com.pisofacil.backend.mapper.PisoMapper;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PisoService {

    private final PisoRepository pisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PisoMapper pisoMapper;

    @Transactional(readOnly = true)
    public List<PisoResponseDTO> findAll() {
        return pisoMapper.toResponseDTOList(pisoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PisoResponseDTO findById(Long id) {
        Piso piso = pisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Piso no encontrado con ID: " + id));
        return pisoMapper.toResponseDTO(piso);
    }

    @Transactional
    public PisoResponseDTO create(PisoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        Piso piso = pisoMapper.toEntity(dto);
        piso.setUsuario(usuario);
        
        return pisoMapper.toResponseDTO(pisoRepository.save(piso));
    }

    @Transactional
    public PisoResponseDTO update(Long id, PisoRequestDTO dto) {
        Piso piso = pisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Piso no encontrado con ID: " + id));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        piso.setUsuario(usuario);
        piso.setDireccion(dto.getDireccion());
        piso.setCiudad(dto.getCiudad());
        piso.setCodigoPostal(dto.getCodigoPostal());
        piso.setNumHabitacionesTotal(dto.getNumHabitacionesTotal());
        piso.setNumBanos(dto.getNumBanos());
        piso.setPlanta(dto.getPlanta());
        piso.setSuperficieTotalM2(dto.getSuperficieTotalM2());
        piso.setTieneWifi(dto.getTieneWifi());
        piso.setTieneAscensor(dto.getTieneAscensor());
        piso.setDescripcionGlobal(dto.getDescripcionGlobal());
        piso.setAdmiteFumadores(dto.getAdmiteFumadores());
        piso.setAdmiteMascotas(dto.getAdmiteMascotas());
        piso.setAdmiteParejas(dto.getAdmiteParejas());
        piso.setLgtbiFriendly(dto.getLgtbiFriendly());

        return pisoMapper.toResponseDTO(pisoRepository.save(piso));
    }

    @Transactional
    public void delete(Long id) {
        if (!pisoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Piso no encontrado con ID: " + id);
        }
        pisoRepository.deleteById(id);
    }
}
