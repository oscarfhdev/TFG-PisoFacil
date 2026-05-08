import { Component, inject, OnInit, ViewChild, signal } from '@angular/core';
import { DatePipe, DecimalPipe, CurrencyPipe, UpperCasePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { RouterLink } from '@angular/router';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { PisoService } from '../../services/piso.service';
import { HabitacionService } from '../../services/habitacion.service';
import { MisPisosResponse } from '../../models/mis-pisos.model';
import { HabitacionResponse } from '../../models/habitacion.model';
import { ConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-admin-pisos',
  standalone: true,
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatSnackBarModule,
    DatePipe,
    CurrencyPipe,
    UpperCasePipe,
    RouterLink
  ],
  templateUrl: './admin-pisos.html',
  styleUrl: './admin-pisos.scss',
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', opacity: 0 })),
      state('expanded', style({ height: '*', opacity: 1 })),
      transition('expanded <=> collapsed', animate('220ms cubic-bezier(0.4, 0, 0.2, 1)')),
    ]),
  ]
})
export class AdminPisos implements OnInit {
  private pisoService = inject(PisoService);
  private habitacionService = inject(HabitacionService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  displayedColumns: string[] = ['propietario', 'direccion', 'ciudad', 'habitaciones', 'fecha', 'acciones', 'expand'];
  displayedColumnsWithExpand: string[] = [...this.displayedColumns, 'expandedDetail'];

  dataSource = new MatTableDataSource<MisPisosResponse>();
  expandedRow: MisPisosResponse | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  cargando = signal(true);

  ngOnInit() {
    this.cargarPisos();
  }

  cargarPisos() {
    this.cargando.set(true);
    this.pisoService.findAllAdmin().subscribe({
      next: (pisos) => {
        this.dataSource.data = pisos;
        setTimeout(() => {
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        });
        this.cargando.set(false);
      },
      error: () => {
        this.snackBar.open('Error al cargar los pisos', 'Cerrar', { duration: 4000, panelClass: ['toast-error'] });
        this.cargando.set(false);
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  toggleRow(row: MisPisosResponse) {
    this.expandedRow = this.expandedRow === row ? null : row;
  }

  getHabitacionesDisponibles(hab: HabitacionResponse[]): number {
    return hab?.filter(h => h.estaDisponible).length ?? 0;
  }

  getTotalHabitaciones(piso: MisPisosResponse): number {
    return piso.habitaciones?.length ?? 0;
  }

  // ── Acciones Admin ──

  confirmarEliminarPiso(piso: MisPisosResponse) {
    const totalHabs = this.getTotalHabitaciones(piso);
    const warnings: string[] = [];
    if (totalHabs > 0) {
      warnings.push(`${totalHabs} habitación${totalHabs > 1 ? 'es' : ''} asociada${totalHabs > 1 ? 's' : ''}`);
    }
    warnings.push('Todas las fotos del piso y sus habitaciones');
    warnings.push('El anuncio dejará de ser visible en la plataforma');

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '500px',
      maxWidth: '95vw',
      data: {
        title: 'Eliminar piso',
        message: `¿Estás seguro de que quieres eliminar el piso en "${piso.direccion}"?`,
        warnings,
        confirmText: 'Sí, eliminar',
        cancelText: 'Cancelar',
        confirmColor: 'warn',
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;

      this.pisoService.adminDelete(piso.idPiso).subscribe({
        next: () => {
          this.dataSource.data = this.dataSource.data.filter(p => p.idPiso !== piso.idPiso);
          if (this.expandedRow === piso) this.expandedRow = null;
          this.snackBar.open(`✓ Piso "${piso.direccion}" eliminado correctamente`, 'OK', { duration: 4000, panelClass: ['toast-success'] });
        },
        error: () => this.snackBar.open('✗ Error al eliminar el piso. Inténtalo de nuevo.', 'Cerrar', { duration: 5000, panelClass: ['toast-error'] })
      });
    });
  }

  toggleDisponibilidadHab(piso: MisPisosResponse, hab: HabitacionResponse) {
    this.habitacionService.toggleDisponibilidad(hab.idHabitacion).subscribe({
      next: (updated) => {
        const habIdx = piso.habitaciones.findIndex(h => h.idHabitacion === hab.idHabitacion);
        if (habIdx !== -1) {
          piso.habitaciones[habIdx].estaDisponible = updated.estaDisponible;
          // Force change detection
          this.dataSource.data = [...this.dataSource.data];
        }
        const estado = updated.estaDisponible ? 'visible' : 'oculta';
        this.snackBar.open(`Habitación "${hab.tituloAnuncio}" ahora ${estado}`, 'OK', { duration: 3000, panelClass: ['toast-success'] });
      },
      error: () => this.snackBar.open('Error al cambiar disponibilidad', 'Cerrar', { duration: 4000, panelClass: ['toast-error'] })
    });
  }

  confirmarEliminarHab(piso: MisPisosResponse, hab: HabitacionResponse) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '480px',
      maxWidth: '95vw',
      data: {
        title: 'Eliminar habitación',
        message: `¿Eliminar la habitación "${hab.tituloAnuncio || 'Sin título'}" del piso en "${piso.direccion}"?`,
        warnings: ['Sus fotos y el anuncio dejarán de estar disponibles'],
        confirmText: 'Sí, eliminar',
        cancelText: 'Cancelar',
        confirmColor: 'warn',
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;

      this.habitacionService.adminDelete(hab.idHabitacion).subscribe({
        next: () => {
          piso.habitaciones = piso.habitaciones.filter(h => h.idHabitacion !== hab.idHabitacion);
          this.dataSource.data = [...this.dataSource.data];
          this.snackBar.open('✓ Habitación eliminada correctamente', 'OK', { duration: 4000, panelClass: ['toast-success'] });
        },
        error: () => this.snackBar.open('✗ Error al eliminar la habitación. Inténtalo de nuevo.', 'Cerrar', { duration: 5000, panelClass: ['toast-error'] })
      });
    });
  }
}
