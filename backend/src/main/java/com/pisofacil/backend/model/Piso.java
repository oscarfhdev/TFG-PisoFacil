package com.pisofacil.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Piso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Piso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPiso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private String direccion;
    private String ciudad;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "num_habitaciones_total")
    private Integer numHabitacionesTotal;

    @Column(name = "num_banos")
    private Integer numBanos;

    private Integer planta;

    @Column(name = "superficie_total_m2")
    private Double superficieTotalM2;

    @Column(name = "tiene_wifi")
    private Boolean tieneWifi;

    @Column(name = "tiene_ascensor")
    private Boolean tieneAscensor;

    @Column(name = "descripcion_global", columnDefinition = "TEXT")
    private String descripcionGlobal;

    @Column(name = "admite_fumadores")
    private Boolean admiteFumadores;

    @Column(name = "admite_mascotas")
    private Boolean admiteMascotas;

    @Column(name = "admite_parejas")
    private Boolean admiteParejas;

    @Column(name = "lgtbi_friendly")
    private Boolean lgtbiFriendly;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @JsonIgnore
    @OneToMany(mappedBy = "piso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habitacion> habitaciones;

    @JsonIgnore
    @OneToMany(mappedBy = "piso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}
