package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByPisoIdPiso(Long idPiso);

    List<Habitacion> findByEstaDisponibleTrue();

    List<Habitacion> findByEstaDisponibleTrueAndPisoCiudadIgnoreCase(String ciudad);

    List<Habitacion> findByEstaDisponibleTrueAndPisoCiudadIgnoreCaseAndPrecioMensualLessThanEqual(
            String ciudad, BigDecimal precioMaximo);
}
