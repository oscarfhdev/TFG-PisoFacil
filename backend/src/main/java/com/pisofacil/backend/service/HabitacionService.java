package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.HabitacionRequestDTO;
import com.pisofacil.backend.dto.HabitacionResponseDTO;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.HabitacionSpecification;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionMapper habitacionMapper;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> findDestacadas() {
        return habitacionMapper.toResponseDTOList(habitacionRepository.findDestacadasRandom());
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> findRecomendadas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new com.pisofacil.backend.exception.ResourceNotFoundException("Usuario no encontrado: " + emailUsuario));

        // Cogemos hasta 14 disponibles aleatorias y calculamos afinidad para cada una
        List<Habitacion> candidatas = habitacionRepository.findDestacadasRandom();

        List<HabitacionResponseDTO> resultado = new java.util.ArrayList<>();
        for (Habitacion h : candidatas) {
            HabitacionResponseDTO dto = habitacionMapper.toResponseDTO(h);
            dto.setPorcentajeCompatibilidad(calcularAfinidad(usuario, h));
            resultado.add(dto);
        }
        // Ordenar de mayor a menor afinidad
        resultado.sort((a, b) -> {
            int pa = a.getPorcentajeCompatibilidad() != null ? a.getPorcentajeCompatibilidad() : 0;
            int pb = b.getPorcentajeCompatibilidad() != null ? b.getPorcentajeCompatibilidad() : 0;
            return Integer.compare(pb, pa);
        });
        return resultado;
    }

    private Integer calcularAfinidad(Usuario u, Habitacion h) {
        int puntos = 0;
        int total = 0;

        // Solo evalúa la dimensión si el usuario TIENE el atributo (true)
        if (Boolean.TRUE.equals(u.getTieneMascota()) && h.getPiso() != null && h.getPiso().getAdmiteMascotas() != null) {
            total++;
            if (Boolean.TRUE.equals(h.getPiso().getAdmiteMascotas())) puntos++;
        }
        if (Boolean.TRUE.equals(u.getEsFumador()) && h.getPiso() != null && h.getPiso().getAdmiteFumadores() != null) {
            total++;
            if (Boolean.TRUE.equals(h.getPiso().getAdmiteFumadores())) puntos++;
        }
        if (Boolean.TRUE.equals(u.getTienePareja()) && h.getPiso() != null && h.getPiso().getAdmiteParejas() != null) {
            total++;
            if (Boolean.TRUE.equals(h.getPiso().getAdmiteParejas())) puntos++;
        }
        if (Boolean.TRUE.equals(u.getPerfilLgtbi()) && h.getPiso() != null && h.getPiso().getLgtbiFriendly() != null) {
            total++;
            if (Boolean.TRUE.equals(h.getPiso().getLgtbiFriendly())) puntos++;
        }

        // Sin dimensiones activas → no hay base para calcular, no mostrar badge
        if (total == 0) return null;
        return (int) Math.round((double) puntos / total * 100);
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> findAll() {
        return habitacionMapper.toResponseDTOList(habitacionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public HabitacionResponseDTO findById(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));
        return habitacionMapper.toResponseDTO(habitacion);
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> findDisponibles() {
        return habitacionMapper.toResponseDTOList(habitacionRepository.findByEstaDisponibleTrue());
    }

    /**
     * Búsqueda avanzada con Motor de Compatibilidad.
     * 
     * Los hard filters (ciudad, precio, baño, exterior, aire) se aplican vía Specification.
     * Si hay un usuario autenticado, se calcula un porcentaje de compatibilidad
     * comparando sus atributos personales con las normas de convivencia del piso.
     * 
     * @param usuario el usuario autenticado, o null si es anónimo
     */
    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> buscarAvanzado(
            String ciudad, BigDecimal precioMin, BigDecimal precioMax,
            Boolean tieneBanoPrivado, Boolean exterior,
            Boolean tieneAireAcondicionado, Boolean tieneCalefaccion,
            Boolean amueblada, Boolean tieneWifi, Boolean tieneAscensor,
            Integer numHabitacionesMax, String centroInteres,
            Usuario usuario) {

        // 1. Aplicar Hard Filters mediante Specification
        Specification<Habitacion> spec = HabitacionSpecification.buildFiltro(
            ciudad, precioMin, precioMax, tieneBanoPrivado, exterior,
            tieneAireAcondicionado, tieneCalefaccion, amueblada,
            tieneWifi, tieneAscensor, numHabitacionesMax, centroInteres);

        List<Habitacion> habitaciones = habitacionRepository.findAll(spec);

        // 2. Mapear a DTOs
        List<HabitacionResponseDTO> dtos = habitacionMapper.toResponseDTOList(habitaciones);

        // 3. Si no hay usuario autenticado, devolver sin porcentaje
        if (usuario == null) {
            return dtos;
        }

        // 4. Motor de Compatibilidad: calcular score para cada habitación
        for (int i = 0; i < dtos.size(); i++) {
            HabitacionResponseDTO dto = dtos.get(i);
            Habitacion hab = habitaciones.get(i);
            dto.setPorcentajeCompatibilidad(calcularCompatibilidad(usuario, hab.getPiso()));
        }

        // 5. Ordenar por compatibilidad descendente
        dtos.sort(Comparator.comparingInt(
            (HabitacionResponseDTO d) -> d.getPorcentajeCompatibilidad() != null ? d.getPorcentajeCompatibilidad() : 0
        ).reversed());

        return dtos;
    }

    /**
     * Algoritmo de compatibilidad entre un usuario y las normas del piso.
     *
     * Evalúa hasta 4 dimensiones (mascotas, fumador, pareja, LGTBI).
     * Una dimensión solo cuenta si el usuario TIENE ese atributo en true (es relevante para él).
     *
     * - Si el usuario no tiene ninguna dimensión activa → null (sin datos para evaluar)
     * - Si tiene dimensiones activas → porcentaje de las que NO generan conflicto con el piso
     *
     * Así se evita el falso 100% para usuarios sin perfil configurado.
     */
    private Integer calcularCompatibilidad(Usuario usuario, Piso piso) {
        int puntos = 0;
        int total = 0;

        // Mascota: solo cuenta si el usuario TIENE mascota (true)
        if (Boolean.TRUE.equals(usuario.getTieneMascota()) && piso.getAdmiteMascotas() != null) {
            total++;
            if (Boolean.TRUE.equals(piso.getAdmiteMascotas())) puntos++;
        }

        // Fumador: solo cuenta si el usuario ES fumador (true)
        if (Boolean.TRUE.equals(usuario.getEsFumador()) && piso.getAdmiteFumadores() != null) {
            total++;
            if (Boolean.TRUE.equals(piso.getAdmiteFumadores())) puntos++;
        }

        // Pareja: solo cuenta si el usuario TIENE pareja (true)
        if (Boolean.TRUE.equals(usuario.getTienePareja()) && piso.getAdmiteParejas() != null) {
            total++;
            if (Boolean.TRUE.equals(piso.getAdmiteParejas())) puntos++;
        }

        // LGTBI: solo cuenta si el usuario SE IDENTIFICA (true)
        if (Boolean.TRUE.equals(usuario.getPerfilLgtbi()) && piso.getLgtbiFriendly() != null) {
            total++;
            if (Boolean.TRUE.equals(piso.getLgtbiFriendly())) puntos++;
        }

        // Sin dimensiones activas → no hay base para calcular compatibilidad
        if (total == 0) return null;

        return (int) Math.round((double) puntos / total * 100);
    }

    @Transactional
    public HabitacionResponseDTO create(HabitacionRequestDTO dto) {
        Piso piso = pisoRepository.findById(dto.getIdPiso())
                .orElseThrow(() -> new ResourceNotFoundException("Piso no encontrado con ID: " + dto.getIdPiso()));

        Habitacion habitacion = habitacionMapper.toEntity(dto);
        habitacion.setPiso(piso);

        return habitacionMapper.toResponseDTO(habitacionRepository.save(habitacion));
    }

    @Transactional
    public HabitacionResponseDTO update(Long id, HabitacionRequestDTO dto) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));

        Piso piso = pisoRepository.findById(dto.getIdPiso())
                .orElseThrow(() -> new ResourceNotFoundException("Piso no encontrado con ID: " + dto.getIdPiso()));

        habitacion.setPiso(piso);
        habitacion.setTituloAnuncio(dto.getTituloAnuncio());
        habitacion.setPrecioMensual(dto.getPrecioMensual());
        habitacion.setDescripcionEspecifica(dto.getDescripcionEspecifica());
        if (dto.getEstaDisponible() != null) {
            habitacion.setEstaDisponible(dto.getEstaDisponible());
        }
        habitacion.setSuperficieM2(dto.getSuperficieM2());
        habitacion.setTieneBanoPrivado(dto.getTieneBanoPrivado());
        habitacion.setAmueblada(dto.getAmueblada());
        habitacion.setExterior(dto.getExterior());
        habitacion.setTieneCalefaccion(dto.getTieneCalefaccion());
        habitacion.setTieneAireAcondicionado(dto.getTieneAireAcondicionado());

        return habitacionMapper.toResponseDTO(habitacionRepository.save(habitacion));
    }

    @Transactional
    public void delete(Long id) {
        if (!habitacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Habitación no encontrada con ID: " + id);
        }
        habitacionRepository.deleteById(id);
    }

    @Transactional
    public HabitacionResponseDTO toggleDisponibilidad(Long id, String emailSolicitante) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));

        Usuario solicitante = usuarioRepository.findByEmail(emailSolicitante)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + emailSolicitante));

        boolean esAdmin = Boolean.TRUE.equals(solicitante.getEsAdmin());
        boolean esPropietario = habitacion.getPiso() != null &&
                habitacion.getPiso().getUsuario() != null &&
                habitacion.getPiso().getUsuario().getIdUsuario().equals(solicitante.getIdUsuario());

        if (!esAdmin && !esPropietario) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No tienes permiso para modificar esta habitación");
        }

        habitacion.setEstaDisponible(!Boolean.TRUE.equals(habitacion.getEstaDisponible()));
        return habitacionMapper.toResponseDTO(habitacionRepository.save(habitacion));
    }
}
