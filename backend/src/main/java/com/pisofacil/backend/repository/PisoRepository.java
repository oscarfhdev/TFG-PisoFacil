package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Piso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PisoRepository extends JpaRepository<Piso, Long> {

    List<Piso> findByUsuarioIdUsuario(Long idUsuario);

    List<Piso> findByCiudad(String ciudad);
}
