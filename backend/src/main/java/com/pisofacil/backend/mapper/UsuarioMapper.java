package com.pisofacil.backend.mapper;

import com.pisofacil.backend.dto.UsuarioDTO;
import com.pisofacil.backend.model.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {
        // Clase de utilidad, no instanciable
    }

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .esAdmin(usuario.getEsAdmin())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .genero(usuario.getGenero())
                .estudios(usuario.getEstudios())
                .biografia(usuario.getBiografia())
                .fotoPerfilUrl(usuario.getFotoPerfilUrl())
                .instagramUrl(usuario.getInstagramUrl())
                .fechaRegistro(usuario.getFechaRegistro())
                .esFumador(usuario.getEsFumador())
                .tieneMascota(usuario.getTieneMascota())
                .tienePareja(usuario.getTienePareja())
                .perfilLgtbi(usuario.getPerfilLgtbi())
                .build();
    }

    /**
     * Convierte un DTO a entidad para actualización de perfil.
     * No incluye password ni email (esos no se actualizan desde el perfil).
     */
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        return Usuario.builder()
                .nombre(dto.getNombre())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .estudios(dto.getEstudios())
                .biografia(dto.getBiografia())
                .fotoPerfilUrl(dto.getFotoPerfilUrl())
                .instagramUrl(dto.getInstagramUrl())
                .esFumador(dto.getEsFumador())
                .tieneMascota(dto.getTieneMascota())
                .tienePareja(dto.getTienePareja())
                .perfilLgtbi(dto.getPerfilLgtbi())
                .build();
    }
}
