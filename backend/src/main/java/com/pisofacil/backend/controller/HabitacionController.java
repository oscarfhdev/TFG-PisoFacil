package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> findAll() {
        return ResponseEntity.ok(habitacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.findById(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<HabitacionResponseDTO>> findDisponibles() {
        return ResponseEntity.ok(habitacionService.findDisponibles());
    }

    @GetMapping("/destacadas")
    public ResponseEntity<List<HabitacionResponseDTO>> findDestacadas() {
        return ResponseEntity.ok(habitacionService.findDestacadas());
    }

    @GetMapping("/recomendadas")
    public ResponseEntity<List<HabitacionResponseDTO>> findRecomendadas() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        return ResponseEntity.ok(habitacionService.findRecomendadas(email));
    }

    /**
     * Endpoint público de búsqueda avanzada con Motor de Compatibilidad.
     * Si el usuario está autenticado (envía JWT válido), se calcula el % de compatibilidad.
     * Si es anónimo, se devuelven los resultados sin porcentaje.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<HabitacionResponseDTO>> buscarAvanzado(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) Boolean tieneBanoPrivado,
            @RequestParam(required = false) Boolean exterior,
            @RequestParam(required = false) Boolean tieneAireAcondicionado,
            @RequestParam(required = false) Boolean tieneCalefaccion,
            @RequestParam(required = false) Boolean amueblada,
            @RequestParam(required = false) Boolean tieneWifi,
            @RequestParam(required = false) Boolean tieneAscensor,
            @RequestParam(required = false) Integer numHabitacionesMax,
            @RequestParam(required = false) String centroInteres) {

        Usuario usuario = obtenerUsuarioAutenticado();

        return ResponseEntity.ok(habitacionService.buscarAvanzado(
            ciudad, precioMin, precioMax, tieneBanoPrivado, exterior,
            tieneAireAcondicionado, tieneCalefaccion, amueblada,
            tieneWifi, tieneAscensor, numHabitacionesMax, centroInteres, usuario));
    }

    /**
     * Extrae el usuario autenticado del SecurityContextHolder.
     * Retorna null si no hay autenticación o el usuario es anónimo.
     */
    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        Object principal = auth.getPrincipal();
        String email = null;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof String str) {
            email = str;
        }

        if (email == null) {
            return null;
        }

        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> create(@Valid @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.ok(habitacionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habitacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/toggle-disponibilidad")
    public ResponseEntity<HabitacionResponseDTO> toggleDisponibilidad(
            @PathVariable Long id,
            Authentication authentication) {
        // Permitir al propietario o a un admin cambiar la disponibilidad
        String emailSolicitante = authentication.getName();
        return ResponseEntity.ok(habitacionService.toggleDisponibilidad(id, emailSolicitante));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable Long id) {
        habitacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
