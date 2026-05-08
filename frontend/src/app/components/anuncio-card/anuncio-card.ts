import { Component, input, computed, output, inject, effect } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AnuncioCardData } from '../../models/anuncio-card.model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';
import { FavoritoService } from '../../services/favorito.service';
import { AlertModal } from '../alert-modal/alert-modal';

@Component({
  selector: 'app-anuncio-card',
  standalone: true,
  imports: [RouterLink, MatDialogModule, MatIconModule],
  templateUrl: './anuncio-card.html',
  styleUrl: './anuncio-card.scss',
})
export class AnuncioCard {
  anuncio = input.required<AnuncioCardData>();
  toggleFavorito = output<number>();

  private dialog = inject(MatDialog);
  private authService = inject(AuthService);
  private favoritoService = inject(FavoritoService);

  // Acceso al estado de autenticación para uso en template
  isLoggedIn = this.authService.isLoggedIn;

  detailLink = computed(() => {
    const a = this.anuncio();
    return a.tipo === 'habitacion'
      ? `/habitacion/${a.id}`
      : `/piso/${a.id}`;
  });

  isFav = computed(() => {
    const a = this.anuncio();
    if (a.esFavorito !== undefined) {
      return a.esFavorito;
    }
    if (a.tipo === 'habitacion') {
      return this.favoritoService.favoritosIds().has(a.id);
    }
    return false;
  });

  constructor() {
    effect(() => {
      // Sync esFavorito from the global signal if the card doesn't provide it explicitly
      // Actually, since it's an input, we shouldn't mutate it directly.
      // But we can just use the global signal in the template if we wanted.
    });
  }

  /** Devuelve la clase CSS del badge según el porcentaje de compatibilidad */
  getBadgeClass(): string {
    const pct = this.anuncio().porcentajeCompatibilidad;
    if (pct == null) return '';
    if (pct >= 80) return 'badge-high';
    if (pct >= 50) return 'badge-medium';
    return 'badge-low';
  }

  /** Devuelve el icono/emoji del badge según el porcentaje */
  getBadgeIcon(): string {
    const pct = this.anuncio().porcentajeCompatibilidad;
    if (pct == null) return '';
    if (pct >= 80) return '🔥';
    if (pct >= 50) return '✨';
    return '⚡';
  }

  onFavoritoClick(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    
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

    // Emite el evento para que el padre lo maneje si quiere (ej: mis-favoritos)
    this.toggleFavorito.emit(this.anuncio().id);

    // Y también hace la llamada al backend automáticamente si es una habitación
    if (this.anuncio().tipo === 'habitacion') {
      this.favoritoService.toggle(this.anuncio().id).subscribe();
    }
  }
}
