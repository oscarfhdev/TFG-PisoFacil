package com.pisofacil.backend.controller;

import com.pisofacil.backend.dto.FotoRequestDTO;
import com.pisofacil.backend.dto.FotoResponseDTO;
import com.pisofacil.backend.service.FotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fotos")
@RequiredArgsConstructor
public class FotoController {

    private final FotoService fotoService;

    @GetMapping("/piso/{idPiso}")
    public ResponseEntity<List<FotoResponseDTO>> findByPiso(@PathVariable Long idPiso) {
        return ResponseEntity.ok(fotoService.findByPiso(idPiso));
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<FotoResponseDTO>> findByHabitacion(@PathVariable Long idHabitacion) {
        return ResponseEntity.ok(fotoService.findByHabitacion(idHabitacion));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoResponseDTO> create(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("idPiso") Long idPiso,
            @RequestParam(value = "idHabitacion", required = false) Long idHabitacion,
            @RequestParam(value = "esPrincipal", required = false) Boolean esPrincipal) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoService.create(archivo, idPiso, idHabitacion, esPrincipal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fotoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
