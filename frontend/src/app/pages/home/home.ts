import { Component, inject, computed } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { HabitacionService } from '../../services/habitacion.service';
import { catchError, of } from 'rxjs';
import { AnuncioCard } from '../../components/anuncio-card/anuncio-card';
import { AnuncioCardData } from '../../models/anuncio-card.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, AnuncioCard, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  private habitacionService = inject(HabitacionService);
  private router = inject(Router);

  ciudadBuscar = '';
  precioMaxBuscar: number | null = null;

  habitacionesDestacadas = toSignal(
    this.habitacionService.findDestacadas().pipe(
      catchError(() => of([]))
    ),
    { initialValue: [] }
  );

  anuncios = computed<AnuncioCardData[]>(() =>
    this.habitacionesDestacadas().map(hab => {
      // Intentar coger la primera foto de la habitación, si no la del piso, si no un placeholder
      const fotoPrincipal = hab.fotosHabitacion && hab.fotosHabitacion.length > 0 
        ? hab.fotosHabitacion[0] 
        : (hab.fotosPiso && hab.fotosPiso.length > 0 ? hab.fotosPiso[0] : 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800');

      return {
        id: hab.idHabitacion,
        tipo: 'habitacion',
        titulo: hab.tituloAnuncio,
        precio: hab.precioMensual,
        ciudad: hab.ciudad,
        direccion: hab.direccion,
        imagenUrl: fotoPrincipal,
        numHabitaciones: hab.numHabitacionesTotal,
        numBanos: hab.tieneBanoPrivado ? 1 : undefined,
        superficieM2: hab.superficieM2,
        estaDisponible: hab.estaDisponible,
        disponibleYa: hab.estaDisponible,
        gastosIncluidos: false, // Default false por ahora
        avatarPropietario: hab.fotoPerfilUrlPropietario || `https://ui-avatars.com/api/?name=Propietario&background=random`,
        afinidad: {
          aceptaMascotas: hab.admiteMascotas || false,
          permiteFumar: hab.admiteFumadores || false,
          lgtbi: hab.lgtbiFriendly || false
        }
      };
    })
  );

  buscar(): void {
    const queryParams: any = {};
    if (this.ciudadBuscar) {
      queryParams.ciudad = this.ciudadBuscar;
    }
    if (this.precioMaxBuscar) {
      queryParams.precioMax = this.precioMaxBuscar;
    }
    this.router.navigate(['/buscar'], { queryParams });
  }
}
