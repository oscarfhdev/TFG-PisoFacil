package com.pisofacil.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Foto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_piso", nullable = false)
    private Piso piso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;

    @Column(name = "url_almacenamiento", nullable = false)
    private String urlAlmacenamiento;
}
