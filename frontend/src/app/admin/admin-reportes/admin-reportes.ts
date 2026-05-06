import { Component, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { ReporteService } from '../../services/reporte.service';
import { ReporteResponse } from '../../models/reporte.model';
import { catchError, map, of } from 'rxjs';

@Component({
  selector: 'app-admin-reportes',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './admin-reportes.html',
  styleUrl: './admin-reportes.scss',
})
export class AdminReportes {
  private reporteService = inject(ReporteService);

  // Cargamos los reportes ordenados del más reciente al más antiguo
  reportes = toSignal(
    this.reporteService.findAll().pipe(
      map(reportes => reportes.sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime())),
      catchError(() => of([] as ReporteResponse[]))
    ),
    { initialValue: [] as ReporteResponse[] }
  );

  cambiarEstado(id: number, nuevoEstado: string) {
    if (confirm(`¿Estás seguro de marcar este reporte como ${nuevoEstado}?`)) {
      this.reporteService.updateEstado(id, nuevoEstado).subscribe({
        next: () => {
          // Idealmente, se recargarían los reportes llamando de nuevo a findAll
          // o mutando el signal si usáramos un WritableSignal.
          // Para esta demostración, recargamos la página o dependemos de la recarga manual.
          window.location.reload();
        },
        error: (err) => console.error('Error actualizando estado del reporte', err)
      });
    }
  }
}
