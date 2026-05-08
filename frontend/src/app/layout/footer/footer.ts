import { Component, inject } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';
import { CrearReporteModal } from '../../components/crear-reporte-modal/crear-reporte-modal';

import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [MatDialogModule, CommonModule, RouterModule],
  templateUrl: './footer.html',
  styleUrl: './footer.scss',
})
export class Footer {
  logoError = false;
  private dialog = inject(MatDialog);
  private authService = inject(AuthService);
  private snackBar = inject(MatSnackBar);

  abrirReporte() {
    if (!this.authService.isLoggedIn()) {
      this.snackBar.open('Debes iniciar sesión para reportar un problema', 'OK', { duration: 4000 });
      return;
    }
    this.dialog.open(CrearReporteModal, {
      width: '500px',
      panelClass: ['premium-dialog-container', 'dark-dialog']
    });
  }
}
