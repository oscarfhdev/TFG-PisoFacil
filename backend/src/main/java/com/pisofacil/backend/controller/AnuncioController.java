package com.pisofacil.backend.controller;

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

    @PostMapping("/publicar")
    public ResponseEntity<PublicarAnuncioResponseDTO> publicar(@Valid @RequestBody PublicarAnuncioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncioService.publicar(dto));
    }
}
