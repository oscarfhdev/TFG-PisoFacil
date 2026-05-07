package com.pisofacil.backend.repository;

import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class HabitacionSpecification {

    public static Specification<Habitacion> buildFiltro(
            String ciudad, BigDecimal precioMin, BigDecimal precioMax,
            Boolean tieneBanoPrivado, Boolean exterior,
            Boolean tieneAireAcondicionado, Boolean admiteMascotas,
            Boolean admiteFumadores, Boolean lgtbiFriendly) {

        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction(); // WHERE true

            // Siempre filtrar solo disponibles
            predicate = cb.and(predicate, cb.isTrue(root.get("estaDisponible")));

            // JOIN con Piso (para ciudad y campos de convivencia)
            Join<Habitacion, Piso> piso = root.join("piso", JoinType.INNER);

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
            if (tieneBanoPrivado != null && tieneBanoPrivado) {
                predicate = cb.and(predicate, cb.isTrue(root.get("tieneBanoPrivado")));
            }
            if (exterior != null && exterior) {
                predicate = cb.and(predicate, cb.isTrue(root.get("exterior")));
            }
            if (tieneAireAcondicionado != null && tieneAireAcondicionado) {
                predicate = cb.and(predicate, cb.isTrue(root.get("tieneAireAcondicionado")));
            }
            // Campos del Piso padre
            if (admiteMascotas != null && admiteMascotas) {
                predicate = cb.and(predicate, cb.isTrue(piso.get("admiteMascotas")));
            }
            if (admiteFumadores != null && admiteFumadores) {
                predicate = cb.and(predicate, cb.isTrue(piso.get("admiteFumadores")));
            }
            if (lgtbiFriendly != null && lgtbiFriendly) {
                predicate = cb.and(predicate, cb.isTrue(piso.get("lgtbiFriendly")));
            }

            return predicate;
        };
    }
}
