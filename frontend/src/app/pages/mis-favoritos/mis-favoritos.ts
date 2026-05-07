import { Component, inject, OnInit, signal } from '@angular/core';
import { FavoritoService } from '../../services/favorito.service';
import { AnuncioCardData } from '../../models/anuncio-card.model';
import { AnuncioCard } from '../../components/anuncio-card/anuncio-card';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-mis-favoritos',
  standalone: true,
  imports: [AnuncioCard, RouterLink],
  templateUrl: './mis-favoritos.html',
  styleUrl: './mis-favoritos.scss',
})
export class MisFavoritos implements OnInit {
  private favoritoService = inject(FavoritoService);

  favoritos = signal<AnuncioCardData[]>([]);
  loading = signal(true);

  ngOnInit() {
    this.cargarFavoritos();
  }

  cargarFavoritos() {
    this.favoritoService.getMisFavoritos().subscribe({
      next: (data) => {
        const cards: AnuncioCardData[] = data.map(f => ({
          id: f.idHabitacion,
          tipo: 'habitacion',
          titulo: f.tituloAnuncio,
          precio: f.precioMensual,
          ciudad: f.ciudad,
          direccion: f.direccion || '',
          imagenUrl: f.fotoPrincipal || 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800',
          superficieM2: f.superficieM2,
          estaDisponible: f.estaDisponible ?? true,
          disponibleYa: f.estaDisponible ?? true,
          gastosIncluidos: false,
          avatarPropietario: f.fotoPerfilUrlPropietario || `https://ui-avatars.com/api/?name=User&background=random`,
          afinidad: {
            aceptaMascotas: f.admiteMascotas ?? false,
            permiteFumar: f.admiteFumadores ?? false,
            lgtbi: f.lgtbiFriendly ?? false
          },
          esFavorito: true
        }));
        this.favoritos.set(cards);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error cargando favoritos', err);
        this.loading.set(false);
      }
    });
  }

  onToggleFavorito(idHabitacion: number) {
    this.favoritos.update(favs => favs.filter(f => f.id !== idHabitacion));
  }
}
