package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {

    List<Foto> findByPisoIdPiso(Long idPiso);

    List<Foto> findByHabitacionIdHabitacion(Long idHabitacion);

    /** Comprueba si ya existe una foto principal para un piso (fotos de zonas comunes, habitacion IS NULL) */
    boolean existsByPisoIdPisoAndHabitacionIsNullAndEsPrincipalTrue(Long idPiso);

    /** Comprueba si ya existe una foto principal para una habitación concreta */
    boolean existsByHabitacionIdHabitacionAndEsPrincipalTrue(Long idHabitacion);

    /** Desmarca todas las fotos principales de un piso (ámbito: zonas comunes) */
    @Modifying
    @Query("UPDATE Foto f SET f.esPrincipal = false WHERE f.piso.idPiso = :idPiso AND f.habitacion IS NULL")
    void clearPrincipalByPiso(@Param("idPiso") Long idPiso);

    /** Desmarca todas las fotos principales de una habitación concreta */
    @Modifying
    @Query("UPDATE Foto f SET f.esPrincipal = false WHERE f.habitacion.idHabitacion = :idHabitacion")
    void clearPrincipalByHabitacion(@Param("idHabitacion") Long idHabitacion);
}
