import { Component, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { ReporteService } from '../../services/reporte.service';
import { ReporteResponse } from '../../models/reporte.model';
import { ConfirmDialogComponent, ConfirmDialogData } from '../../components/confirm-dialog/confirm-dialog';
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
  private dialog = inject(MatDialog);

  // Cargamos los reportes ordenados del más reciente al más antiguo
  reportes = toSignal(
    this.reporteService.findAll().pipe(
      map(reportes => reportes.sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime())),
      catchError(() => of([] as ReporteResponse[]))
    ),
    { initialValue: [] as ReporteResponse[] }
  );

  cambiarEstado(id: number, nuevoEstado: string) {
    const esResolv = nuevoEstado.toUpperCase() === 'RESUELTO';

    const data: ConfirmDialogData = {
      title: esResolv ? 'Resolver denuncia' : 'Rechazar denuncia',
      message: esResolv
        ? '¿Confirmas que este reporte ha sido revisado y queda marcado como resuelto?'
        : '¿Confirmas que deseas rechazar este reporte y cerrarlo sin acción?',
      confirmText: esResolv ? 'Sí, resolver' : 'Sí, rechazar',
      cancelText: 'Cancelar',
      confirmColor: esResolv ? 'primary' : 'warn',
      confirmIcon: esResolv ? 'check_circle' : 'cancel',
    };

    const ref = this.dialog.open(ConfirmDialogComponent, { data, autoFocus: false });

    ref.afterClosed().subscribe(confirmed => {
      if (!confirmed) return;
      this.reporteService.updateEstado(id, nuevoEstado).subscribe({
        next: () => window.location.reload(),
        error: (err) => console.error('Error actualizando estado del reporte', err),
      });
    });
  }
}
