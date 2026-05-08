import { Component, inject, OnInit, ViewChild, signal } from '@angular/core';
import { DatePipe, DecimalPipe, CurrencyPipe, UpperCasePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterLink } from '@angular/router';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { PisoService } from '../../services/piso.service';
import { MisPisosResponse } from '../../models/mis-pisos.model';
import { HabitacionResponse } from '../../models/habitacion.model';

@Component({
  selector: 'app-admin-pisos',
  standalone: true,
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatSnackBarModule,
    DatePipe,
    DecimalPipe,
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
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['propietario', 'direccion', 'ciudad', 'habitaciones', 'fecha', 'expand'];
  displayedColumnsWithExpand: string[] = [...this.displayedColumns, 'expandedDetail'];
  habitacionColumns: string[] = ['titulo', 'precio', 'superficie', 'bano', 'disponibilidad'];

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
}
