package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuarioIdUsuario(Long idUsuario);

    Optional<Favorito> findByUsuarioIdUsuarioAndHabitacionIdHabitacion(Long idUsuario, Long idHabitacion);

    boolean existsByUsuarioIdUsuarioAndHabitacionIdHabitacion(Long idUsuario, Long idHabitacion);

    void deleteByHabitacionIdHabitacion(Long idHabitacion);
}
