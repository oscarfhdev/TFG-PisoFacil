package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByUsuarioEmisorIdUsuario(Long idUsuario);

    List<Reporte> findByEstado(String estado);
}
