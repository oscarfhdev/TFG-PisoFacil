package com.pisofacil.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.exception.GlobalExceptionHandler;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.service.HabitacionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración web para HabitacionController.
 * Levanta solo la capa web (sin BD). Los filtros de seguridad (JWT)
 * se desactivan vía addFilters=false para aislar el controlador.
 *
 * Para los endpoints que reciben {@code Authentication} como parámetro,
 * se inyecta manualmente vía {@code SecurityMockMvcRequestPostProcessors.authentication()}.
 */
@WebMvcTest(HabitacionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class HabitacionControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private HabitacionService habitacionService;
    @MockitoBean private UsuarioRepository usuarioRepository;

    // Beans de seguridad que Spring intenta inyectar — los mockeamos
    @MockitoBean private com.pisofacil.backend.security.JwtAuthenticationFilter jwtFilter;
    @MockitoBean private com.pisofacil.backend.security.JwtUtil jwtUtil;
    @MockitoBean private com.pisofacil.backend.security.PisoFacilUserDetailsService userDetailsService;

    // ─── Fixture ───
    private HabitacionResponseDTO buildSampleDTO() {
        return HabitacionResponseDTO.builder()
                .idHabitacion(1L).idPiso(10L)
                .tituloAnuncio("Habitación luminosa")
                .precioMensual(new BigDecimal("400.00"))
                .estaDisponible(true).ciudad("Madrid")
                .build();
    }

    // ══════════════════════════════════════════════════════════════
    //  GET /habitaciones → 200
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /habitaciones → 200 OK con lista")
    void findAll_devuelve200() throws Exception {
        when(habitacionService.findAll()).thenReturn(List.of(buildSampleDTO()));

        mockMvc.perform(get("/habitaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tituloAnuncio", is("Habitación luminosa")));
    }

    @Test
    @DisplayName("GET /habitaciones → 200 OK lista vacía")
    void findAll_listaVacia_devuelve200() throws Exception {
        when(habitacionService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/habitaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ══════════════════════════════════════════════════════════════
    //  GET /habitaciones/{id} → 200 / 404
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /habitaciones/1 → 200 OK")
    void findById_existente_devuelve200() throws Exception {
        when(habitacionService.findById(1L)).thenReturn(buildSampleDTO());

        mockMvc.perform(get("/habitaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHabitacion", is(1)))
                .andExpect(jsonPath("$.ciudad", is("Madrid")));
    }

    @Test
    @DisplayName("GET /habitaciones/999 → 404 Not Found")
    void findById_noExiste_devuelve404() throws Exception {
        when(habitacionService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Habitación no encontrada con ID: 999"));

        mockMvc.perform(get("/habitaciones/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("999")));
    }

    // ══════════════════════════════════════════════════════════════
    //  POST /habitaciones → 201 / 400
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /habitaciones → 201 Created con body válido")
    void create_bodyValido_devuelve201() throws Exception {
        HabitacionRequestDTO request = HabitacionRequestDTO.builder()
                .idPiso(10L).tituloAnuncio("Nueva habitación")
                .precioMensual(new BigDecimal("350.00")).build();

        when(habitacionService.create(any(HabitacionRequestDTO.class))).thenReturn(buildSampleDTO());

        mockMvc.perform(post("/habitaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idHabitacion", is(1)));
    }

    @Test
    @DisplayName("POST /habitaciones → 400 Bad Request sin campos obligatorios")
    void create_sinTitulo_devuelve400() throws Exception {
        HabitacionRequestDTO request = HabitacionRequestDTO.builder().build();

        mockMvc.perform(post("/habitaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ══════════════════════════════════════════════════════════════
    //  DELETE /habitaciones/{id} → 204 / 404
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("DELETE /habitaciones/1 → 204 No Content")
    void delete_existente_devuelve204() throws Exception {
        doNothing().when(habitacionService).delete(1L);

        mockMvc.perform(delete("/habitaciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /habitaciones/999 → 404 Not Found")
    void delete_noExiste_devuelve404() throws Exception {
        doThrow(new ResourceNotFoundException("Habitación no encontrada con ID: 999"))
                .when(habitacionService).delete(999L);

        mockMvc.perform(delete("/habitaciones/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("999")));
    }

    // ══════════════════════════════════════════════════════════════
    //  GET /habitaciones/disponibles → 200
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /habitaciones/disponibles → 200 OK solo disponibles")
    void findDisponibles_devuelve200() throws Exception {
        HabitacionResponseDTO disponible = buildSampleDTO();
        disponible.setEstaDisponible(true);
        when(habitacionService.findDisponibles()).thenReturn(List.of(disponible));

        mockMvc.perform(get("/habitaciones/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estaDisponible", is(true)));
    }

    // ══════════════════════════════════════════════════════════════
    //  GET /habitaciones/buscar → 200 con filtros
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /habitaciones/buscar?ciudad=Madrid → 200 OK con resultados filtrados")
    void buscarAvanzado_conCiudad_devuelve200() throws Exception {
        when(habitacionService.buscarAvanzado(
                eq("Madrid"), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(buildSampleDTO()));

        mockMvc.perform(get("/habitaciones/buscar").param("ciudad", "Madrid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ciudad", is("Madrid")));
    }
}
