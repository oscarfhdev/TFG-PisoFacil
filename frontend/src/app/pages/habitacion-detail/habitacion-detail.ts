import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { HabitacionService } from '../../services/habitacion.service';
import { FavoritoService } from '../../services/favorito.service';
import { UsuarioService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';
import { HabitacionResponse } from '../../models/habitacion.model';
import { UsuarioModal } from '../../components/usuario-modal/usuario-modal';
import { ContactoModal } from '../../components/contacto-modal/contacto-modal';
import { AlertModal } from '../../components/alert-modal/alert-modal';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-habitacion-detail',
  standalone: true,
  imports: [
    RouterLink,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    NgClass
  ],
  templateUrl: './habitacion-detail.html',
  styleUrl: './habitacion-detail.scss',
})
export class HabitacionDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private habitacionService = inject(HabitacionService);
  private favoritoService = inject(FavoritoService);
  private usuarioService = inject(UsuarioService);
  private authService = inject(AuthService);
  private dialog = inject(MatDialog);

  habitacion = signal<HabitacionResponse | null>(null);
  loading = signal(true);
  
  // Galería
  todasLasFotos = signal<string[]>([]);
  fotoActivaIndex = signal(0);
  
  // Favoritos
  esFavorito = signal(false);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.cargarDatos(id);
    }
  }

  private cargarDatos(id: number) {
    this.habitacionService.findById(id).subscribe({
      next: (data) => {
        this.habitacion.set(data);
        
        // Combinar fotos: PISO PRIMERO (la principal del anuncio siempre es la del piso),
        // luego las de la habitación.
        // El backend ya ordena fotosPiso con la principal primero (esPrincipal=true).
        const fotosPiso = data.fotosPiso || [];
        const fotosHab = data.fotosHabitacion || [];
        
        // Si no hay fotos, poner un placeholder
        if (fotosPiso.length === 0 && fotosHab.length === 0) {
          this.todasLasFotos.set(['https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1200']);
        } else {
          this.todasLasFotos.set([...fotosPiso, ...fotosHab]);
        }
        
        this.checkFavorito();
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error cargando la habitación', err);
        this.loading.set(false);
      }
    });
  }

  private checkFavorito() {
    const usuarioId = this.authService.currentUser()?.idUsuario;
    const habId = this.habitacion()?.idHabitacion;
    if (usuarioId && habId) {
      this.favoritoService.checkMiFavorito(habId).subscribe({
        next: (isFav) => this.esFavorito.set(isFav)
      });
    }
  }

  toggleFavorito() {
    if (!this.authService.currentUser()) {
      this.dialog.open(AlertModal, {
        data: {
          title: 'Inicia sesión',
          message: 'Debes iniciar sesión para añadir habitaciones a tus favoritos.',
          icon: 'favorite_border',
          iconColor: 'text-red-500'
        },
        width: '400px',
        panelClass: 'modal-propietario'
      });
      return;
    }

    const habId = this.habitacion()?.idHabitacion;
    if (!habId) return;

    // Optimistic update
    const prev = this.esFavorito();
    this.esFavorito.set(!prev);

    this.favoritoService.toggle(habId).subscribe({
      next: (res) => {
        this.esFavorito.set(res.esFavorito);
      },
      error: () => {
        // Rollback
        this.esFavorito.set(prev);
      }
    });
  }

  setFotoActiva(index: number) {
    this.fotoActivaIndex.set(index);
  }

  abrirModalPropietario() {
    // Comprobar si el usuario está registrado (logueado)
    if (!this.authService.currentUser()) {
      this.dialog.open(AlertModal, {
        data: {
          title: 'Acceso Restringido',
          message: 'No puedes ver los datos detallados del anunciante si no estás registrado. Por favor, inicia sesión para continuar.',
          icon: 'lock',
          iconColor: 'text-amber-500'
        },
        width: '400px',
        panelClass: 'modal-propietario'
      });
      return;
    }

    const hab = this.habitacion();
    if (!hab || !hab.idUsuarioPropietario) return;

    // Cargamos el perfil completo antes de abrir el modal
    this.usuarioService.findById(hab.idUsuarioPropietario).subscribe({
      next: (usuario) => {
        this.dialog.open(UsuarioModal, {
          data: usuario,
          width: '480px',
          panelClass: 'modal-propietario'
        });
      },
      error: (err) => console.error('Error cargando propietario', err)
    });
  }

  abrirModalContacto() {
    if (!this.authService.currentUser()) {
      this.dialog.open(AlertModal, {
        data: {
          title: 'Acceso Restringido',
          message: 'Debes iniciar sesión para ver los métodos de contacto del anunciante.',
          icon: 'lock',
          iconColor: 'text-amber-500'
        },
        width: '400px',
        panelClass: 'modal-propietario'
      });
      return;
    }

    const hab = this.habitacion();
    if (!hab || !hab.idUsuarioPropietario) return;

    this.usuarioService.findById(hab.idUsuarioPropietario).subscribe({
      next: (usuario) => {
        this.dialog.open(ContactoModal, {
          data: usuario,
          width: '480px',
          panelClass: 'modal-propietario'
        });
      },
      error: (err) => console.error('Error cargando propietario', err)
    });
  }
}
