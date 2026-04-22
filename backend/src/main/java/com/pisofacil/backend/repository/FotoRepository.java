package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {

    List<Foto> findByPisoIdPiso(Long idPiso);

    List<Foto> findByHabitacionIdHabitacion(Long idHabitacion);
}
