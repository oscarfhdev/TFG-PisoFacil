import { Component, inject, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HabitacionService } from '../../services/habitacion.service';
import { BusquedaFiltros } from '../../models/habitacion.model';
import { AnuncioCardData } from '../../models/anuncio-card.model';
import { AnuncioCard } from '../../components/anuncio-card/anuncio-card';
import { toSignal } from '@angular/core/rxjs-interop';

import { MatSliderModule } from '@angular/material/slider';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { catchError, debounceTime, distinctUntilChanged, Subject, switchMap, of, tap } from 'rxjs';

@Component({
  selector: 'app-buscar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    AnuncioCard,
    MatSliderModule,
    MatCheckboxModule,
    MatInputModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatDividerModule
  ],
  templateUrl: './buscar.html',
  styleUrl: './buscar.scss'
})
export class Buscar {
  private habitacionService = inject(HabitacionService);
  private route = inject(ActivatedRoute);

  // Filtros Signals
  ciudad = signal<string>('');
  precioMin = signal<number | null>(null);
  precioMax = signal<number | null>(null);
  tieneBanoPrivado = signal<boolean>(false);
  exterior = signal<boolean>(false);
  tieneAireAcondicionado = signal<boolean>(false);
  admiteMascotas = signal<boolean>(false);
  admiteFumadores = signal<boolean>(false);
  lgtbiFriendly = signal<boolean>(false);

  loading = signal<boolean>(false);
  
  // Subject for debouncing search requests
  private searchSubject = new Subject<BusquedaFiltros>();

  // Use toSignal for declarative reactive pipeline
  resultadosRaw = toSignal(
    this.searchSubject.pipe(
      debounceTime(400),
      tap(() => this.loading.set(true)),
      switchMap(filtros => this.habitacionService.buscarAvanzado(filtros).pipe(
        catchError(() => of([]))
      )),
      tap(() => this.loading.set(false))
    ),
    { initialValue: [] }
  );

  anuncios = computed<AnuncioCardData[]>(() => {
    return this.resultadosRaw().map(hab => {
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
        gastosIncluidos: false,
        avatarPropietario: hab.fotoPerfilUrlPropietario || `https://ui-avatars.com/api/?name=Propietario&background=random`,
        afinidad: {
          aceptaMascotas: hab.admiteMascotas || false,
          permiteFumar: hab.admiteFumadores || false,
          lgtbi: hab.lgtbiFriendly || false,
          admiteParejas: false
        },
        amueblada: hab.amueblada,
        exterior: hab.exterior,
        tieneCalefaccion: hab.tieneCalefaccion,
        tieneAireAcondicionado: hab.tieneAireAcondicionado,
        tieneWifi: hab.tieneWifi,
        tieneAscensor: hab.tieneAscensor,
        tieneBanoPrivado: hab.tieneBanoPrivado
      };
    });
  });

  constructor() {
    // 1. Initialize from query params
    this.route.queryParamMap.subscribe(params => {
      const pCiudad = params.get('ciudad');
      const pPrecioMax = params.get('precioMax');
      
      if (pCiudad) this.ciudad.set(pCiudad);
      if (pPrecioMax) this.precioMax.set(Number(pPrecioMax));
      
      this.triggerSearch();
    });
  }

  // Called from template when any filter changes
  onFilterChange() {
    this.triggerSearch();
  }

  private triggerSearch() {
    const filtros: BusquedaFiltros = {};
    if (this.ciudad()) filtros.ciudad = this.ciudad();
    if (this.precioMin()) filtros.precioMin = this.precioMin()!;
    if (this.precioMax()) filtros.precioMax = this.precioMax()!;
    if (this.tieneBanoPrivado()) filtros.tieneBanoPrivado = true;
    if (this.exterior()) filtros.exterior = true;
    if (this.tieneAireAcondicionado()) filtros.tieneAireAcondicionado = true;
    if (this.admiteMascotas()) filtros.admiteMascotas = true;
    if (this.admiteFumadores()) filtros.admiteFumadores = true;
    if (this.lgtbiFriendly()) filtros.lgtbiFriendly = true;

    this.searchSubject.next(filtros);
  }
}
