package com.pisofacil.backend.service;

import com.pisofacil.backend.dto.FotoRequestDTO;
import com.pisofacil.backend.dto.FotoResponseDTO;
import com.pisofacil.backend.mapper.FotoMapper;
import com.pisofacil.backend.model.Foto;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.repository.FotoRepository;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FotoService {

    private final FotoRepository fotoRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionRepository habitacionRepository;
    private final FotoMapper fotoMapper;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public List<FotoResponseDTO> findByPiso(Long idPiso) {
        return fotoMapper.toResponseDTOList(fotoRepository.findByPisoIdPiso(idPiso));
    }

    @Transactional(readOnly = true)
    public List<FotoResponseDTO> findByHabitacion(Long idHabitacion) {
        return fotoMapper.toResponseDTOList(fotoRepository.findByHabitacionIdHabitacion(idHabitacion));
    }

    @Transactional
    public FotoResponseDTO create(MultipartFile archivo, Long idPiso, Long idHabitacion, Boolean esPrincipalParam) {
        Piso piso = pisoRepository.findById(idPiso)
                .orElseThrow(() -> new ResourceNotFoundException("Piso no encontrado con ID: " + idPiso));

        Habitacion habitacion = null;
        if (idHabitacion != null) {
            habitacion = habitacionRepository.findById(idHabitacion)
                    .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + idHabitacion));
        }

        String urlAlmacenamiento = storageService.store(archivo);

        // La foto principal SOLO aplica a fotos del piso (zonas comunes).
        // Las fotos de habitación NUNCA son principales: la imagen destacada
        // del anuncio siempre es la foto principal del piso.
        boolean esPrincipal = false;
        if (habitacion == null) {
            // Es una foto del piso
            if (Boolean.TRUE.equals(esPrincipalParam)) {
                // El usuario marcó esta foto como principal → desmarcar la anterior
                fotoRepository.clearPrincipalByPiso(piso.getIdPiso());
                esPrincipal = true;
            } else if (esPrincipalParam == null) {
                // Auto-asignar: primera foto del piso = principal
                esPrincipal = !fotoRepository.existsByPisoIdPisoAndHabitacionIsNullAndEsPrincipalTrue(piso.getIdPiso());
            }
            // Si esPrincipalParam == false, se queda en false
        }

        Foto foto = new Foto();
        foto.setPiso(piso);
        foto.setHabitacion(habitacion);
        foto.setUrlAlmacenamiento(urlAlmacenamiento);
        foto.setEsPrincipal(esPrincipal);

        return fotoMapper.toResponseDTO(fotoRepository.save(foto));
    }

    @Transactional
    public void delete(Long id) {
        if (!fotoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Foto no encontrada con ID: " + id);
        }
        fotoRepository.deleteById(id);
    }
}
