package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.AnuncioRequest;
import com.pisofacil.backend.dto.HabitacionDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.mapper.PisoMapper;
import com.pisofacil.backend.model.Foto;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.service.AnuncioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/anuncios")
@RequiredArgsConstructor
public class AnuncioController {

    private final AnuncioService anuncioService;

    /**
     * POST /api/anuncios?idUsuario=X
     * Publica un anuncio completo (Piso + Habitación + Fotos).
     */
    @PostMapping
    public ResponseEntity<HabitacionDTO> publicarAnuncio(
            @RequestParam Long idUsuario,
            @RequestBody AnuncioRequest request) {

        // Mapper: DTO → Entidades
        Piso piso = PisoMapper.fromAnuncioRequest(request);
        Habitacion habitacion = HabitacionMapper.fromAnuncioRequest(request);

        // Convertir URLs de fotos a entidades Foto
        List<Foto> fotos = new ArrayList<>();
        if (request.getFotosUrls() != null) {
            for (String url : request.getFotosUrls()) {
                Foto foto = Foto.builder()
                        .urlAlmacenamiento(url)
                        .build();
                fotos.add(foto);
            }
        }

        // Service: lógica de negocio
        Habitacion resultado = anuncioService.publicarAnuncio(idUsuario, piso, habitacion, fotos);

        // Mapper: Entidad → DTO
        return new ResponseEntity<>(HabitacionMapper.toDTO(resultado), HttpStatus.CREATED);
    }

    /**
     * GET /api/anuncios/disponibles
     * Lista todas las habitaciones disponibles.
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<HabitacionDTO>> listarDisponibles() {
        List<HabitacionDTO> habitaciones = anuncioService.listarHabitacionesDisponibles()
                .stream()
                .map(HabitacionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(habitaciones);
    }

    /**
     * DELETE /api/anuncios/{idHabitacion}
     * Elimina un anuncio (habitación) y sus dependencias.
     */
    @DeleteMapping("/{idHabitacion}")
    public ResponseEntity<Void> eliminarAnuncio(@PathVariable Long idHabitacion) {
        anuncioService.eliminarAnuncio(idHabitacion);
        return ResponseEntity.noContent().build();
    }
}
