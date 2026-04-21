package com.pisofacil.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Column(name = "es_admin")
    private Boolean esAdmin = false;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String genero;

    private String estudios;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "es_fumador")
    private Boolean esFumador;

    @Column(name = "tiene_mascota")
    private Boolean tieneMascota;

    @Column(name = "tiene_pareja")
    private Boolean tienePareja;

    @Column(name = "perfil_lgtbi")
    private Boolean perfilLgtbi;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Piso> pisos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorito> favoritos;

    @OneToMany(mappedBy = "usuarioEmisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reporte> reportes;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (esAdmin == null) {
            esAdmin = false;
        }
    }
}
