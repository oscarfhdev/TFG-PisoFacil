package com.pisofacil.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "Favorito",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_usuario", "id_habitacion"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFavorito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;
}
