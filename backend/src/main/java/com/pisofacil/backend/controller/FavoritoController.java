package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.FavoritoRequestDTO;
import com.pisofacil.backend.dto.FavoritoResponseDTO;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.service.FavoritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    @GetMapping("/me")
    public ResponseEntity<List<FavoritoResponseDTO>> getMisFavoritos() {
        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(favoritoService.findByUsuario(usuario.getIdUsuario()));
    }

    @PostMapping("/toggle/{idHabitacion}")
    public ResponseEntity<Map<String, Boolean>> toggleFavorito(@PathVariable Long idHabitacion) {
        Usuario usuario = getUsuarioAutenticado();
        boolean esFavorito = favoritoService.toggle(usuario.getIdUsuario(), idHabitacion);
        return ResponseEntity.ok(Map.of("esFavorito", esFavorito));
    }

    @GetMapping("/check-auth")
    public ResponseEntity<Boolean> checkFavoritoAuth(@RequestParam Long idHabitacion) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            return ResponseEntity.ok(favoritoService.checkFavorito(usuario.getIdUsuario(), idHabitacion));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<FavoritoResponseDTO>> findByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(favoritoService.findByUsuario(idUsuario));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorito(
            @RequestParam Long idUsuario,
            @RequestParam Long idHabitacion) {
        return ResponseEntity.ok(favoritoService.checkFavorito(idUsuario, idHabitacion));
    }

    @PostMapping
    public ResponseEntity<FavoritoResponseDTO> create(@Valid @RequestBody FavoritoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoritoService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        favoritoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
