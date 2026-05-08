import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { HabitacionService } from '../../services/habitacion.service';
import { AuthService } from '../../services/auth.service';
import { BusquedaFiltros } from '../../models/habitacion.model';
import { AnuncioCardData } from '../../models/anuncio-card.model';
import { AnuncioCard } from '../../components/anuncio-card/anuncio-card';
import { toSignal } from '@angular/core/rxjs-interop';

import { MatSliderModule } from '@angular/material/slider';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { catchError, debounceTime, Subject, switchMap, of, tap } from 'rxjs';

@Component({
  selector: 'app-buscar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    AnuncioCard,
    MatSliderModule,
    MatInputModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatIconModule,
  ],
  templateUrl: './buscar.html',
  styleUrl: './buscar.scss'
})
export class Buscar {
  private habitacionService = inject(HabitacionService);
  private route = inject(ActivatedRoute);
  authService = inject(AuthService);

  // --- Hard Filters Signals ---
  ciudad = signal<string>('');
  precioMin = signal<number>(100);
  precioMax = signal<number>(1000);

  // Booleanos — Habitación
  tieneBanoPrivado = signal<boolean>(false);
  exterior         = signal<boolean>(false);
  tieneAireAcondicionado = signal<boolean>(false);
  tieneCalefaccion = signal<boolean>(false);
  amueblada        = signal<boolean>(false);

  // Booleanos — Piso
  tieneWifi     = signal<boolean>(false);
  tieneAscensor = signal<boolean>(false);

  // Numérico — tamaño del piso
  numHabitacionesMax = signal<number | null>(null);

  // Texto — centro de interés
  centroInteres = signal<string>('');

  loading = signal<boolean>(false);

  private searchSubject = new Subject<BusquedaFiltros>();

  resultadosRaw = toSignal(
    this.searchSubject.pipe(
      debounceTime(350),
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
      const fotoPrincipal =
        hab.fotosHabitacion?.length ? hab.fotosHabitacion[0]
        : hab.fotosPiso?.length     ? hab.fotosPiso[0]
        : 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800';

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
        avatarPropietario: hab.fotoPerfilUrlPropietario
          || `https://ui-avatars.com/api/?name=Propietario&background=random`,
        afinidad: {
          aceptaMascotas: hab.admiteMascotas  || false,
          permiteFumar:   hab.admiteFumadores || false,
          lgtbi:          hab.lgtbiFriendly   || false,
          admiteParejas:  hab.admiteParejas   || false,
        },
        amueblada:             hab.amueblada,
        exterior:              hab.exterior,
        tieneCalefaccion:      hab.tieneCalefaccion,
        tieneAireAcondicionado:hab.tieneAireAcondicionado,
        tieneWifi:             hab.tieneWifi,
        tieneAscensor:         hab.tieneAscensor,
        tieneBanoPrivado:      hab.tieneBanoPrivado,
        porcentajeCompatibilidad: hab.porcentajeCompatibilidad,
      };
    });
  });

  /** Número de filtros activos */
  activeFiltersCount = computed(() => {
    let n = 0;
    if (this.ciudad())                n++;
    if (this.precioMin() > 100)       n++;
    if (this.precioMax() < 1000)      n++;
    if (this.tieneBanoPrivado())      n++;
    if (this.exterior())              n++;
    if (this.tieneAireAcondicionado())n++;
    if (this.tieneCalefaccion())      n++;
    if (this.amueblada())             n++;
    if (this.tieneWifi())             n++;
    if (this.tieneAscensor())         n++;
    if (this.numHabitacionesMax())    n++;
    if (this.centroInteres())         n++;
    return n;
  });

  constructor() {
    this.route.queryParamMap.subscribe(params => {
      const pCiudad   = params.get('ciudad');
      const pPrecioMax = params.get('precioMax');
      if (pCiudad)    this.ciudad.set(pCiudad);
      if (pPrecioMax) this.precioMax.set(Number(pPrecioMax));
      this.triggerSearch();
    });
  }

  onFilterChange() { this.triggerSearch(); }

  toggle(sig: ReturnType<typeof signal<boolean>>) {
    sig.set(!sig());
    this.triggerSearch();
  }

  clearFilters() {
    this.ciudad.set('');
    this.precioMin.set(100);
    this.precioMax.set(1000);
    this.tieneBanoPrivado.set(false);
    this.exterior.set(false);
    this.tieneAireAcondicionado.set(false);
    this.tieneCalefaccion.set(false);
    this.amueblada.set(false);
    this.tieneWifi.set(false);
    this.tieneAscensor.set(false);
    this.numHabitacionesMax.set(null);
    this.centroInteres.set('');
    this.triggerSearch();
  }

  private triggerSearch() {
    const f: BusquedaFiltros = {};
    if (this.ciudad())                  f.ciudad = this.ciudad();
    if (this.precioMin() > 100)         f.precioMin = this.precioMin();
    if (this.precioMax() < 1000)        f.precioMax = this.precioMax();
    if (this.tieneBanoPrivado())        f.tieneBanoPrivado = true;
    if (this.exterior())                f.exterior = true;
    if (this.tieneAireAcondicionado())  f.tieneAireAcondicionado = true;
    if (this.tieneCalefaccion())        f.tieneCalefaccion = true;
    if (this.amueblada())               f.amueblada = true;
    if (this.tieneWifi())               f.tieneWifi = true;
    if (this.tieneAscensor())           f.tieneAscensor = true;
    if (this.numHabitacionesMax())      f.numHabitacionesMax = this.numHabitacionesMax()!;
    if (this.centroInteres())           f.centroInteres = this.centroInteres();
    this.searchSubject.next(f);
  }
}
