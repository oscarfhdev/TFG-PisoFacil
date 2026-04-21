package com.pisofacil.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Habitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabitacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_piso", nullable = false)
    private Piso piso;

    @Column(name = "titulo_anuncio")
    private String tituloAnuncio;

    @Column(name = "precio_mensual")
    private Double precioMensual;

    @Column(name = "descripcion_especifica", columnDefinition = "TEXT")
    private String descripcionEspecifica;

    @Column(name = "esta_disponible")
    private Boolean estaDisponible;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(name = "superficie_m2")
    private Double superficieM2;

    @Column(name = "tiene_bano_privado")
    private Boolean tieneBanoPrivado;

    private Boolean amueblada;
    private Boolean exterior;

    @Column(name = "tiene_calefaccion")
    private Boolean tieneCalefaccion;

    @Column(name = "tiene_aire_acondicionado")
    private Boolean tieneAireAcondicionado;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos;

    @PrePersist
    protected void onCreate() {
        if (fechaPublicacion == null) {
            fechaPublicacion = LocalDateTime.now();
        }
        if (estaDisponible == null) {
            estaDisponible = true;
        }
    }
}
