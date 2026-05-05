package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.UsuarioRequestDTO;
import com.pisofacil.backend.dto.UsuarioResponseDTO;
import com.pisofacil.backend.mapper.UsuarioMapper;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        return usuarioMapper.toResponseDTOList(usuarioRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(saved);
    }

    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setGenero(dto.getGenero());
        usuario.setEstudios(dto.getEstudios());
        usuario.setBiografia(dto.getBiografia());
        usuario.setFotoPerfilUrl(dto.getFotoPerfilUrl());
        usuario.setInstagramUrl(dto.getInstagramUrl());
        usuario.setEsFumador(dto.getEsFumador());
        usuario.setTieneMascota(dto.getTieneMascota());
        usuario.setTienePareja(dto.getTienePareja());
        usuario.setPerfilLgtbi(dto.getPerfilLgtbi());

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
