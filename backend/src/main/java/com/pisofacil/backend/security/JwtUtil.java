package com.pisofacil.backend.security;

import com.pisofacil.backend.model.Usuario;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey key;

    // Se ejecuta después del constructor, cuando @Value ya ha inyectado los valores
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT para el usuario.
     * Subject = email, Claim "role" = ADMIN o USER.
     */
    public String generateToken(Usuario usuario) {
        String role = Boolean.TRUE.equals(usuario.getEsAdmin()) ? "ADMIN" : "USER";

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("role", role)
                .claim("idUsuario", usuario.getIdUsuario())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    /**
     * Valida la firma y la expiración del token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Extrae el email (subject) del token.
     */
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
}
