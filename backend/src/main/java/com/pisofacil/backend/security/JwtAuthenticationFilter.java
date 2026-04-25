package com.pisofacil.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // A. Obtener el header Authorization
        String authHeader = request.getHeader("Authorization");

        // B. Comprobar si viene el token (Bearer xxx)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // C. Extraer el token (quitar "Bearer ")
        String token = authHeader.substring(7);

        // D. Validar token y autenticar
        if (jwtUtil.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtUtil.extractUsername(token);

            // E. Cargar usuario de la base de datos
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // F. Crear objeto de autenticación
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // G. Guardar en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // H. Pasar al siguiente filtro
        filterChain.doFilter(request, response);
    }
}
