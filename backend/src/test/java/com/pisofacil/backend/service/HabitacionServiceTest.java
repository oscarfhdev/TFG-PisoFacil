package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Batería exhaustiva de pruebas unitarias para HabitacionService.
 * Usa Mockito para aislar completamente la capa de servicio.
 */
@ExtendWith(MockitoExtension.class)
class HabitacionServiceTest {

    @Mock private HabitacionRepository habitacionRepository;
    @Mock private PisoRepository pisoRepository;
    @Mock private HabitacionMapper habitacionMapper;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks
    private HabitacionService habitacionService;

    // ─── Fixtures reutilizables ───
    private Usuario propietario;
    private Piso piso;
    private Habitacion habitacion;
    private HabitacionResponseDTO responseDTO;
    private HabitacionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        propietario = Usuario.builder()
                .idUsuario(1L).nombre("Carlos").apellidos("García")
                .email("carlos@test.com").password("hashed")
                .esAdmin(false).cuentaActiva(true)
                .tieneMascota(true).esFumador(false)
                .tienePareja(true).perfilLgtbi(false)
                .build();

        piso = Piso.builder()
                .idPiso(10L).usuario(propietario)
                .ciudad("Madrid").direccion("Calle Gran Vía 1")
                .admiteMascotas(true).admiteFumadores(false)
                .admiteParejas(true).lgtbiFriendly(true)
                .build();

        habitacion = Habitacion.builder()
                .idHabitacion(100L).piso(piso)
                .tituloAnuncio("Hab. luminosa centro")
                .precioMensual(new BigDecimal("450.00"))
                .estaDisponible(true)
                .build();

        responseDTO = HabitacionResponseDTO.builder()
                .idHabitacion(100L).idPiso(10L)
                .tituloAnuncio("Hab. luminosa centro")
                .precioMensual(new BigDecimal("450.00"))
                .estaDisponible(true).ciudad("Madrid")
                .build();

        requestDTO = HabitacionRequestDTO.builder()
                .idPiso(10L)
                .tituloAnuncio("Hab. luminosa centro")
                .precioMensual(new BigDecimal("450.00"))
                .build();
    }

    // ══════════════════════════════════════════════════════════════
    //  1. HAPPY PATH — CRUD básico
    // ══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Happy Path — CRUD")
    class HappyPath {

        @Test
        @DisplayName("findAll → devuelve lista de DTOs")
        void findAll_devuelveListaDTOs() {
            when(habitacionRepository.findAll()).thenReturn(List.of(habitacion));
            when(habitacionMapper.toResponseDTOList(any())).thenReturn(List.of(responseDTO));

            List<HabitacionResponseDTO> resultado = habitacionService.findAll();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getTituloAnuncio()).isEqualTo("Hab. luminosa centro");
            verify(habitacionRepository).findAll();
        }

        @Test
        @DisplayName("findById → devuelve DTO cuando el ID existe")
        void findById_existente_devuelveDTO() {
            when(habitacionRepository.findById(100L)).thenReturn(Optional.of(habitacion));
            when(habitacionMapper.toResponseDTO(habitacion)).thenReturn(responseDTO);

            HabitacionResponseDTO resultado = habitacionService.findById(100L);

            assertThat(resultado.getIdHabitacion()).isEqualTo(100L);
            assertThat(resultado.getCiudad()).isEqualTo("Madrid");
        }

        @Test
        @DisplayName("findDisponibles → solo habitaciones disponibles")
        void findDisponibles_devuelveSoloDisponibles() {
            when(habitacionRepository.findByEstaDisponibleTrue()).thenReturn(List.of(habitacion));
            when(habitacionMapper.toResponseDTOList(any())).thenReturn(List.of(responseDTO));

            List<HabitacionResponseDTO> resultado = habitacionService.findDisponibles();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getEstaDisponible()).isTrue();
        }

        @Test
        @DisplayName("create → persiste y devuelve DTO con 201 implícito")
        void create_pisoExistente_devuelveDTO() {
            when(pisoRepository.findById(10L)).thenReturn(Optional.of(piso));
            when(habitacionMapper.toEntity(requestDTO)).thenReturn(habitacion);
            when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacion);
            when(habitacionMapper.toResponseDTO(habitacion)).thenReturn(responseDTO);

            HabitacionResponseDTO resultado = habitacionService.create(requestDTO);

            assertThat(resultado.getTituloAnuncio()).isEqualTo("Hab. luminosa centro");
            verify(habitacionRepository).save(any(Habitacion.class));
        }

        @Test
        @DisplayName("delete → elimina cuando el ID existe")
        void delete_existente_eliminaSinError() {
            when(habitacionRepository.existsById(100L)).thenReturn(true);

            assertThatCode(() -> habitacionService.delete(100L)).doesNotThrowAnyException();
            verify(habitacionRepository).deleteById(100L);
        }

        @Test
        @DisplayName("update → actualiza campos y persiste")
        void update_existente_actualizaCampos() {
            HabitacionRequestDTO updateDTO = HabitacionRequestDTO.builder()
                    .idPiso(10L).tituloAnuncio("Título actualizado")
                    .precioMensual(new BigDecimal("500.00"))
                    .estaDisponible(false).build();

            HabitacionResponseDTO updatedResponse = HabitacionResponseDTO.builder()
                    .idHabitacion(100L).tituloAnuncio("Título actualizado")
                    .precioMensual(new BigDecimal("500.00")).estaDisponible(false).build();

            when(habitacionRepository.findById(100L)).thenReturn(Optional.of(habitacion));
            when(pisoRepository.findById(10L)).thenReturn(Optional.of(piso));
            when(habitacionRepository.save(any())).thenReturn(habitacion);
            when(habitacionMapper.toResponseDTO(any())).thenReturn(updatedResponse);

            HabitacionResponseDTO resultado = habitacionService.update(100L, updateDTO);

            assertThat(resultado.getTituloAnuncio()).isEqualTo("Título actualizado");
            verify(habitacionRepository).save(any());
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  2. EXCEPCIONES Y CASOS LÍMITE
    // ══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Excepciones y Casos Límite")
    class Excepciones {

        @Test
        @DisplayName("findById → lanza ResourceNotFoundException si ID no existe")
        void findById_noExiste_lanzaExcepcion() {
            when(habitacionRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> habitacionService.findById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("999");
        }

        @Test
        @DisplayName("create → lanza ResourceNotFoundException si el Piso no existe")
        void create_pisoNoExiste_lanzaExcepcion() {
            when(pisoRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> habitacionService.create(requestDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Piso no encontrado");
        }

        @Test
        @DisplayName("delete → lanza ResourceNotFoundException si ID no existe")
        void delete_noExiste_lanzaExcepcion() {
            when(habitacionRepository.existsById(999L)).thenReturn(false);

            assertThatThrownBy(() -> habitacionService.delete(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("999");
        }

        @Test
        @DisplayName("findAll → devuelve lista vacía si no hay habitaciones")
        void findAll_sinDatos_devuelveListaVacia() {
            when(habitacionRepository.findAll()).thenReturn(Collections.emptyList());
            when(habitacionMapper.toResponseDTOList(any())).thenReturn(Collections.emptyList());

            assertThat(habitacionService.findAll()).isEmpty();
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  3. TOGGLE DISPONIBILIDAD + PERMISOS
    // ══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("toggleDisponibilidad — Permisos")
    class ToggleDisponibilidad {

        @Test
        @DisplayName("Propietario puede cambiar disponibilidad")
        void toggle_propietario_cambia() {
            when(habitacionRepository.findById(100L)).thenReturn(Optional.of(habitacion));
            when(usuarioRepository.findByEmail("carlos@test.com")).thenReturn(Optional.of(propietario));
            when(habitacionRepository.save(any())).thenReturn(habitacion);
            when(habitacionMapper.toResponseDTO(any())).thenReturn(responseDTO);

            assertThatCode(() -> habitacionService.toggleDisponibilidad(100L, "carlos@test.com"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Usuario no propietario ni admin → AccessDeniedException")
        void toggle_sinPermisos_lanzaExcepcion() {
            Usuario intruso = Usuario.builder()
                    .idUsuario(99L).email("intruso@test.com").esAdmin(false).build();

            when(habitacionRepository.findById(100L)).thenReturn(Optional.of(habitacion));
            when(usuarioRepository.findByEmail("intruso@test.com")).thenReturn(Optional.of(intruso));

            assertThatThrownBy(() -> habitacionService.toggleDisponibilidad(100L, "intruso@test.com"))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        @DisplayName("Admin puede cambiar disponibilidad de cualquier habitación")
        void toggle_admin_cambia() {
            Usuario admin = Usuario.builder()
                    .idUsuario(2L).email("admin@test.com").esAdmin(true).build();

            when(habitacionRepository.findById(100L)).thenReturn(Optional.of(habitacion));
            when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
            when(habitacionRepository.save(any())).thenReturn(habitacion);
            when(habitacionMapper.toResponseDTO(any())).thenReturn(responseDTO);

            assertThatCode(() -> habitacionService.toggleDisponibilidad(100L, "admin@test.com"))
                    .doesNotThrowAnyException();
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  4. MOTOR DE COMPATIBILIDAD (findRecomendadas)
    // ══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Motor de Compatibilidad — calcularAfinidad")
    class MotorCompatibilidad {

        @Test
        @DisplayName("Compatible 100% → usuario con mascota+pareja, piso admite ambas")
        void recomendadas_compatible100() {
            // El usuario tiene mascota=true, pareja=true; piso admite ambas
            when(usuarioRepository.findByEmail("carlos@test.com")).thenReturn(Optional.of(propietario));
            when(habitacionRepository.findDestacadasRandom()).thenReturn(List.of(habitacion));
            when(habitacionMapper.toResponseDTO(any())).thenReturn(
                    HabitacionResponseDTO.builder().idHabitacion(100L).build());

            List<HabitacionResponseDTO> resultado = habitacionService.findRecomendadas("carlos@test.com");

            assertThat(resultado).hasSize(1);
            // mascota OK + pareja OK = 2/2 = 100%
            assertThat(resultado.get(0).getPorcentajeCompatibilidad()).isEqualTo(100);
        }

        @Test
        @DisplayName("Incompatible por mascotas → 50% (mascota sí, pareja sí, piso no admite mascota)")
        void recomendadas_incompatibleMascotas() {
            Piso pisoRestr = Piso.builder().idPiso(11L)
                    .admiteMascotas(false).admiteParejas(true)
                    .admiteFumadores(null).lgtbiFriendly(null).build();
            Habitacion habRestr = Habitacion.builder()
                    .idHabitacion(101L).piso(pisoRestr).build();

            when(usuarioRepository.findByEmail("carlos@test.com")).thenReturn(Optional.of(propietario));
            when(habitacionRepository.findDestacadasRandom()).thenReturn(List.of(habRestr));
            when(habitacionMapper.toResponseDTO(any())).thenReturn(
                    HabitacionResponseDTO.builder().idHabitacion(101L).build());

            List<HabitacionResponseDTO> resultado = habitacionService.findRecomendadas("carlos@test.com");

            // mascota NO + pareja OK = 1/2 = 50%
            assertThat(resultado.get(0).getPorcentajeCompatibilidad()).isEqualTo(50);
        }

        @Test
        @DisplayName("Incompatible 0% → fumador en piso que no admite fumadores")
        void recomendadas_incompatibleFumador() {
            Usuario fumador = Usuario.builder()
                    .idUsuario(3L).email("fuma@test.com")
                    .esFumador(true).tieneMascota(false)
                    .tienePareja(false).perfilLgtbi(false).build();

            Piso pisoNoFuma = Piso.builder().idPiso(12L)
                    .admiteFumadores(false).admiteMascotas(null)
                    .admiteParejas(null).lgtbiFriendly(null).build();
            Habitacion habNoFuma = Habitacion.builder()
                    .idHabitacion(102L).piso(pisoNoFuma).build();

            when(usuarioRepository.findByEmail("fuma@test.com")).thenReturn(Optional.of(fumador));
            when(habitacionRepository.findDestacadasRandom()).thenReturn(List.of(habNoFuma));
            when(habitacionMapper.toResponseDTO(any())).thenReturn(
                    HabitacionResponseDTO.builder().idHabitacion(102L).build());

            List<HabitacionResponseDTO> resultado = habitacionService.findRecomendadas("fuma@test.com");

            // fumador NO admitido = 0/1 = 0%
            assertThat(resultado.get(0).getPorcentajeCompatibilidad()).isEqualTo(0);
        }

        @Test
        @DisplayName("Sin dimensiones activas → porcentaje null (sin badge)")
        void recomendadas_sinDimensiones_devuelveNull() {
            Usuario sinPerfil = Usuario.builder()
                    .idUsuario(4L).email("vacio@test.com")
                    .esFumador(false).tieneMascota(false)
                    .tienePareja(false).perfilLgtbi(false).build();

            when(usuarioRepository.findByEmail("vacio@test.com")).thenReturn(Optional.of(sinPerfil));
            when(habitacionRepository.findDestacadasRandom()).thenReturn(List.of(habitacion));
            when(habitacionMapper.toResponseDTO(any())).thenReturn(
                    HabitacionResponseDTO.builder().idHabitacion(100L).build());

            List<HabitacionResponseDTO> resultado = habitacionService.findRecomendadas("vacio@test.com");

            assertThat(resultado.get(0).getPorcentajeCompatibilidad()).isNull();
        }
    }
}
