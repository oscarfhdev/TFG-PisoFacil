package com.pisofacil.backend.security;

import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PisoFacilUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscar usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // 2. Determinar el rol según el boolean esAdmin
        String role = Boolean.TRUE.equals(usuario.getEsAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";

        // 3. Traducir al formato que Spring Security entiende
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .build();
    }
}
