import { Component, inject, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { HabitacionService } from '../../services/habitacion.service';
import { catchError, of } from 'rxjs';
import { AnuncioCard } from '../../components/anuncio-card/anuncio-card';
import { AnuncioCardData } from '../../models/anuncio-card.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, AnuncioCard],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  private habitacionService = inject(HabitacionService);

  habitacionesDestacadas = toSignal(
    this.habitacionService.findDisponibles().pipe(
      catchError(() => of([]))
    ),
    { initialValue: [] }
  );

  anuncios = computed<AnuncioCardData[]>(() =>
    this.habitacionesDestacadas().map(hab => ({
      id: hab.idHabitacion,
      tipo: 'habitacion',
      titulo: hab.tituloAnuncio,
      precio: hab.precioMensual,
      ciudad: hab.ciudad,
      direccion: hab.direccion,
      imagenUrl: 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800',
      numHabitaciones: undefined,
      numBanos: hab.tieneBanoPrivado ? 1 : 0,
      superficieM2: hab.superficieM2,
      estaDisponible: hab.estaDisponible,
      disponibleYa: true, // Mock property, in a real case comes from backend
      gastosIncluidos: true, // Mock property
      avatarPropietario: `https://ui-avatars.com/api/?name=User+${hab.idPiso}&background=random`,
      afinidad: {
        aceptaMascotas: true, // Mock property
        permiteFumar: false,
        lgtbi: true
      }
    }))
  );
}
