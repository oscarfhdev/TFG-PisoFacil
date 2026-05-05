package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.PublicarAnuncioRequestDTO;
import com.pisofacil.backend.dto.PublicarAnuncioResponseDTO;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import com.pisofacil.backend.mapper.HabitacionMapper;
import com.pisofacil.backend.mapper.PisoMapper;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnuncioService {

    private final UsuarioRepository usuarioRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionRepository habitacionRepository;
    private final PisoMapper pisoMapper;
    private final HabitacionMapper habitacionMapper;

    @Transactional
    public PublicarAnuncioResponseDTO publicar(PublicarAnuncioRequestDTO dto) {
        // 1. Buscar al usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        // 2. Crear y guardar el Piso
        Piso piso = new Piso();
        piso.setUsuario(usuario);
        piso.setDireccion(dto.getDireccion());
        piso.setCiudad(dto.getCiudad());
        piso.setCodigoPostal(dto.getCodigoPostal());
        piso.setNumHabitacionesTotal(dto.getNumHabitacionesTotal());
        piso.setNumBanos(dto.getNumBanos());
        piso.setPlanta(dto.getPlanta());
        piso.setSuperficieTotalM2(dto.getSuperficieTotalM2());
        piso.setTieneWifi(dto.getTieneWifi());
        piso.setTieneAscensor(dto.getTieneAscensor());
        piso.setDescripcionGlobal(dto.getDescripcionGlobal());
        piso.setAdmiteFumadores(dto.getAdmiteFumadores());
        piso.setAdmiteMascotas(dto.getAdmiteMascotas());
        piso.setAdmiteParejas(dto.getAdmiteParejas());
        piso.setLgtbiFriendly(dto.getLgtbiFriendly());

        Piso pisoGuardado = pisoRepository.save(piso);

        // 3. Crear y guardar la primera Habitación vinculada al Piso
        Habitacion habitacion = new Habitacion();
        habitacion.setPiso(pisoGuardado);
        habitacion.setTituloAnuncio(dto.getTituloAnuncioHabitacion());
        habitacion.setPrecioMensual(dto.getPrecioMensualHabitacion());
        habitacion.setDescripcionEspecifica(dto.getDescripcionEspecificaHabitacion());
        habitacion.setSuperficieM2(dto.getSuperficieM2Habitacion());
        habitacion.setTieneBanoPrivado(dto.getTieneBanoPrivadoHabitacion());
        habitacion.setAmueblada(dto.getAmuebladaHabitacion());
        habitacion.setExterior(dto.getExteriorHabitacion());
        habitacion.setTieneCalefaccion(dto.getTieneCalefaccionHabitacion());
        habitacion.setTieneAireAcondicionado(dto.getTieneAireAcondicionadoHabitacion());

        Habitacion habitacionGuardada = habitacionRepository.save(habitacion);

        // 4. Construir y devolver la respuesta con los mappers
        return PublicarAnuncioResponseDTO.builder()
                .piso(pisoMapper.toResponseDTO(pisoGuardado))
                .habitacion(habitacionMapper.toResponseDTO(habitacionGuardada))
                .build();
    }
}
