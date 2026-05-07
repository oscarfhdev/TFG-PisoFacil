package com.pisofacil.backend.controller;

import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.pisofacil.backend.dto.PublicarAnuncioRequestDTO;
import com.pisofacil.backend.dto.PublicarAnuncioResponseDTO;
import com.pisofacil.backend.service.AnuncioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anuncios")
@RequiredArgsConstructor
public class AnuncioController {

    private final AnuncioService anuncioService;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    @PostMapping("/publicar")
    public ResponseEntity<PublicarAnuncioResponseDTO> publicar(@Valid @RequestBody PublicarAnuncioRequestDTO dto) {
        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncioService.publicar(dto, usuario));
    }
}
