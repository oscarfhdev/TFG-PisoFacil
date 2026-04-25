package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.AnuncioRequest;
import com.pisofacil.backend.dto.AnuncioUpdateRequest;
import com.pisofacil.backend.dto.FotoDTO;
import com.pisofacil.backend.dto.HabitacionDTO;
import com.pisofacil.backend.mapper.FotoMapper;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.mapper.PisoMapper;
import com.pisofacil.backend.model.Foto;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.service.AnuncioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            Authentication authentication,
            @RequestBody AnuncioRequest request) {

        Piso piso = PisoMapper.fromAnuncioRequest(request);
        Habitacion habitacion = HabitacionMapper.fromAnuncioRequest(request);

        List<Foto> fotos = new ArrayList<>();
        if (request.getFotosUrls() != null) {
            for (String url : request.getFotosUrls()) {
                Foto foto = Foto.builder()
                        .urlAlmacenamiento(url)
                        .build();
                fotos.add(foto);
            }
        }

        Habitacion resultado = anuncioService.publicarAnuncio(authentication.getName(), piso, habitacion, fotos);
        return new ResponseEntity<>(HabitacionMapper.toDTO(resultado), HttpStatus.CREATED);
    }

    /**
     * GET /api/anuncios/{idHabitacion}
     * Obtiene los datos completos de una habitación (ficha del anuncio).
     */
    @GetMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionDTO> obtenerAnuncio(@PathVariable Long idHabitacion) {
        Habitacion habitacion = anuncioService.obtenerHabitacionPorId(idHabitacion);
        return ResponseEntity.ok(HabitacionMapper.toDTO(habitacion));
    }

    /**
     * GET /api/anuncios/mis-anuncios?idUsuario=X
     * Lista todas las habitaciones publicadas por un usuario (sus anuncios).
     */
    @GetMapping("/mis-anuncios")
    public ResponseEntity<List<HabitacionDTO>> misAnuncios(Authentication authentication) {
        List<HabitacionDTO> anuncios = anuncioService.listarAnunciosPorUsuario(authentication.getName())
                .stream()
                .map(HabitacionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(anuncios);
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
     * PUT /api/anuncios/{idHabitacion}
     * Edita los datos de un anuncio existente (habitación + piso).
     */
    @PutMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionDTO> editarAnuncio(
            @PathVariable Long idHabitacion,
            @RequestBody AnuncioUpdateRequest request) {

        Habitacion actualizada = anuncioService.editarAnuncio(idHabitacion, request);
        return ResponseEntity.ok(HabitacionMapper.toDTO(actualizada));
    }

    /**
     * PUT /api/anuncios/{idHabitacion}/disponibilidad?disponible=true|false
     * Cambia la disponibilidad de una habitación sin borrarla.
     */
    @PutMapping("/{idHabitacion}/disponibilidad")
    public ResponseEntity<HabitacionDTO> cambiarDisponibilidad(
            @PathVariable Long idHabitacion,
            @RequestParam boolean disponible) {

        Habitacion actualizada = anuncioService.cambiarDisponibilidad(idHabitacion, disponible);
        return ResponseEntity.ok(HabitacionMapper.toDTO(actualizada));
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

    /**
     * GET /api/anuncios/fotos/piso/{idPiso}
     * Obtiene las fotos de un piso.
     */
    @GetMapping("/fotos/piso/{idPiso}")
    public ResponseEntity<List<FotoDTO>> fotosPorPiso(@PathVariable Long idPiso) {
        List<FotoDTO> fotos = anuncioService.obtenerFotosPorPiso(idPiso)
                .stream()
                .map(FotoMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(fotos);
    }

    /**
     * GET /api/anuncios/fotos/habitacion/{idHabitacion}
     * Obtiene las fotos de una habitación.
     */
    @GetMapping("/fotos/habitacion/{idHabitacion}")
    public ResponseEntity<List<FotoDTO>> fotosPorHabitacion(@PathVariable Long idHabitacion) {
        List<FotoDTO> fotos = anuncioService.obtenerFotosPorHabitacion(idHabitacion)
                .stream()
                .map(FotoMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(fotos);
    }
}
