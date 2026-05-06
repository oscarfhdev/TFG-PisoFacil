import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { DatePipe, UpperCasePipe } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';
import { PisoService } from '../../services/piso.service';
import { HabitacionService } from '../../services/habitacion.service';
import { ReporteService } from '../../services/reporte.service';
import { catchError, map, of } from 'rxjs';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [DatePipe, UpperCasePipe],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard {
  private usuarioService = inject(UsuarioService);
  private pisoService = inject(PisoService);
  private habitacionService = inject(HabitacionService);
  private reporteService = inject(ReporteService);

  // KPIs
  usuarios = toSignal(
    this.usuarioService.findAll().pipe(catchError(() => of([]))),
    { initialValue: [] }
  );

  pisos = toSignal(
    this.pisoService.findAll().pipe(catchError(() => of([]))),
    { initialValue: [] }
  );

  habitaciones = toSignal(
    this.habitacionService.findDisponibles().pipe(catchError(() => of([]))),
    { initialValue: [] }
  );

  reportes = toSignal(
    this.reporteService.findAll().pipe(
      map(r => r.sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime())),
      catchError(() => of([]))
    ),
    { initialValue: [] }
  );
}
