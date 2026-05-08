import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PisoService } from '../../services/piso.service';
import { HabitacionService } from '../../services/habitacion.service';
import { MisPisosResponse } from '../../models/mis-pisos.model';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-mis-anuncios',
  standalone: true,
  imports: [CommonModule, MatIconModule, RouterLink],
  templateUrl: './mis-anuncios.html',
  styleUrl: './mis-anuncios.scss'
})
export class MisAnuncios implements OnInit {
  private pisoService = inject(PisoService);
  private habitacionService = inject(HabitacionService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  
  pisos = signal<MisPisosResponse[]>([]);
  loading = signal<boolean>(true);

  ngOnInit() {
    this.cargarPisos();
  }

  cargarPisos() {
    this.pisoService.getMyPisos().subscribe({
      next: (data) => {
        this.pisos.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar mis pisos', err);
        this.loading.set(false);
      }
    });
  }

  eliminarPiso(idPiso: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: '¿Eliminar piso?',
        message: 'Esta acción es irreversible. Se eliminarán permanentemente el piso, sus habitaciones y todas las fotos asociadas.',
        confirmText: 'Eliminar',
        cancelText: 'Cancelar',
        confirmColor: 'warn'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.pisoService.delete(idPiso).subscribe({
          next: () => {
            this.snackBar.open('Piso eliminado correctamente', 'Cerrar', {
              duration: 3000,
              panelClass: ['toast-success']
            });
            // Filtrar el piso de la lista local en lugar de recargar todo de nuevo
            this.pisos.update(pisos => pisos.filter(p => p.idPiso !== idPiso));
          },
          error: (err) => {
            console.error('Error al eliminar el piso', err);
            this.snackBar.open('Error al eliminar el piso. Inténtalo de nuevo.', 'Cerrar', {
              duration: 4000,
              panelClass: ['toast-error']
            });
          }
        });
      }
    });
  }
  toggleDisponibilidad(idHabitacion: number, index: number, piso: MisPisosResponse) {
    this.habitacionService.toggleDisponibilidad(idHabitacion).subscribe({
      next: (updated) => {
        // Actualizar estado localmente sin recargar
        const habIndex = piso.habitaciones.findIndex(h => h.idHabitacion === idHabitacion);
        if (habIndex !== -1) {
          piso.habitaciones[habIndex].estaDisponible = updated.estaDisponible;
          // Forzar detección de cambios en la signal
          this.pisos.update(p => [...p]);
        }
        const estado = updated.estaDisponible ? 'disponible' : 'ocupada';
        this.snackBar.open(`Habitación marcada como ${estado}`, 'Cerrar', {
          duration: 3000,
          panelClass: ['toast-success']
        });
      },
      error: (err) => {
        console.error('Error al cambiar disponibilidad', err);
        this.snackBar.open('Error al cambiar el estado. Inténtalo de nuevo.', 'Cerrar', {
          duration: 4000,
          panelClass: ['toast-error']
        });
      }
    });
  }
}
