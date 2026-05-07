import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DatePipe, UpperCasePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioResponse } from '../../models/usuario.model';
import { AdminUsuarioEditModal } from '../admin-usuario-edit-modal/admin-usuario-edit-modal';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule, MatSortModule, MatSnackBarModule, DatePipe, UpperCasePipe],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.scss',
})
export class AdminUsuarios implements OnInit {
  private usuarioService = inject(UsuarioService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  displayedColumns: string[] = ['avatar', 'nombre', 'email', 'fechaRegistro', 'esAdmin', 'estado', 'acciones'];
  dataSource = new MatTableDataSource<UsuarioResponse>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.usuarioService.findAll().subscribe({
      next: (usuarios) => {
        this.dataSource.data = usuarios;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err) => console.error('Error cargando usuarios', err)
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  verDetalles(usuario: UsuarioResponse) {
    const dialogRef = this.dialog.open(AdminUsuarioEditModal, {
      width: '600px',
      maxWidth: '95vw',
      data: { usuario },
      panelClass: 'dark:bg-card-dark' // Asegura buen fondo en modo oscuro
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.updatedUser) {
        // Update the user in the table data
        const idx = this.dataSource.data.findIndex(u => u.idUsuario === result.updatedUser.idUsuario);
        if (idx !== -1) {
          this.dataSource.data[idx] = result.updatedUser;
          this.dataSource.data = [...this.dataSource.data];
        }
      }
    });
  }

  toggleEstado(usuario: UsuarioResponse) {
    this.usuarioService.toggleEstadoCuenta(usuario.idUsuario).subscribe({
      next: (updated) => {
        const idx = this.dataSource.data.findIndex(u => u.idUsuario === updated.idUsuario);
        if (idx !== -1) {
          this.dataSource.data[idx] = updated;
          this.dataSource.data = [...this.dataSource.data];
        }
        const estado = updated.cuentaActiva ? 'activada' : 'suspendida';
        this.snackBar.open(`Cuenta de ${updated.nombre} ${estado}`, 'OK', { duration: 3000 });
      },
      error: () => this.snackBar.open('Error al cambiar el estado', 'Cerrar', { duration: 4000 })
    });
  }
}
