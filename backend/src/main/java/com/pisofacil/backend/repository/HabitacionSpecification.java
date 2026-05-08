package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class HabitacionSpecification {

    /**
     * Construye el filtro con los "Hard Filters" (excluyentes).
     * Los soft filters de convivencia (mascotas, fumadores, lgtbi) se procesan
     * en el Motor de Compatibilidad del servicio.
     */
    public static Specification<Habitacion> buildFiltro(
            String ciudad,
            BigDecimal precioMin, BigDecimal precioMax,
            Boolean tieneBanoPrivado, Boolean exterior,
            Boolean tieneAireAcondicionado, Boolean tieneCalefaccion,
            Boolean amueblada, Boolean tieneWifi, Boolean tieneAscensor,
            Integer numHabitacionesMax,
            String centroInteres) {

        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction(); // WHERE true

            // Siempre filtrar solo disponibles
            predicate = cb.and(predicate, cb.isTrue(root.get("estaDisponible")));

            // JOIN con Piso (para ciudad, wifi, ascensor, etc.)
            Join<Habitacion, Piso> piso = root.join("piso", JoinType.INNER);

            // --- Filtros de texto / rango ---
            if (ciudad != null && !ciudad.isBlank()) {
                predicate = cb.and(predicate,
                    cb.like(cb.lower(piso.get("ciudad")),
                            "%" + ciudad.toLowerCase() + "%"));
            }
            if (precioMin != null) {
                predicate = cb.and(predicate,
                    cb.greaterThanOrEqualTo(root.get("precioMensual"), precioMin));
            }
            if (precioMax != null) {
                predicate = cb.and(predicate,
                    cb.lessThanOrEqualTo(root.get("precioMensual"), precioMax));
            }

            // --- Filtros booleanos de la Habitación ---
            if (Boolean.TRUE.equals(tieneBanoPrivado)) {
                predicate = cb.and(predicate, cb.isTrue(root.get("tieneBanoPrivado")));
            }
            if (Boolean.TRUE.equals(exterior)) {
                predicate = cb.and(predicate, cb.isTrue(root.get("exterior")));
            }
            if (Boolean.TRUE.equals(tieneAireAcondicionado)) {
                predicate = cb.and(predicate, cb.isTrue(root.get("tieneAireAcondicionado")));
            }
            if (Boolean.TRUE.equals(tieneCalefaccion)) {
                predicate = cb.and(predicate, cb.isTrue(root.get("tieneCalefaccion")));
            }
            if (Boolean.TRUE.equals(amueblada)) {
                predicate = cb.and(predicate, cb.isTrue(root.get("amueblada")));
            }

            // --- Filtros booleanos del Piso ---
            if (Boolean.TRUE.equals(tieneWifi)) {
                predicate = cb.and(predicate, cb.isTrue(piso.get("tieneWifi")));
            }
            if (Boolean.TRUE.equals(tieneAscensor)) {
                predicate = cb.and(predicate, cb.isTrue(piso.get("tieneAscensor")));
            }

            // --- Filtro de número de habitaciones máximo del piso (para pisos pequeños) ---
            if (numHabitacionesMax != null && numHabitacionesMax > 0) {
                predicate = cb.and(predicate,
                    cb.lessThanOrEqualTo(piso.get("numHabitacionesTotal"), numHabitacionesMax));
            }

            // --- Centro de interés ---
            if (centroInteres != null && !centroInteres.isBlank()) {
                predicate = cb.and(predicate,
                    cb.like(cb.lower(piso.get("centroInteres")),
                            "%" + centroInteres.toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
