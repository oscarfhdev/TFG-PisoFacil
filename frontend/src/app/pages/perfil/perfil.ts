import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioResponse } from '../../models/usuario.model';
import { PerfilUpdateRequest, CambiarPasswordRequest } from '../../models/perfil-update.model';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [ReactiveFormsModule, MatTabsModule, MatIconModule, MatSnackBarModule, DatePipe],
  templateUrl: './perfil.html',
  styleUrl: './perfil.scss',
})
export class Perfil implements OnInit {
  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);
  private snackBar = inject(MatSnackBar);

  usuario = signal<UsuarioResponse | null>(null);
  
  perfilForm!: FormGroup;
  passwordForm!: FormGroup;

  ngOnInit() {
    this.perfilForm = this.fb.group({
      nombre: ['', Validators.required],
      apellidos: [''],
      email: ['', [Validators.required, Validators.email]],
      fechaNacimiento: [''],
      genero: [''],
      estudios: [''],
      biografia: [''],
      instagramUrl: [''],
      esFumador: [false],
      tieneMascota: [false],
      tienePareja: [false],
      perfilLgtbi: [false]
    });

    this.passwordForm = this.fb.group({
      passwordActual: ['', Validators.required],
      passwordNueva: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.cargarPerfil();
  }

  cargarPerfil() {
    this.usuarioService.getMyProfile().subscribe({
      next: (user) => {
        this.usuario.set(user);
        this.perfilForm.patchValue({
          nombre: user.nombre,
          apellidos: user.apellidos,
          email: user.email,
          fechaNacimiento: user.fechaNacimiento,
          genero: user.genero,
          estudios: user.estudios,
          biografia: user.biografia,
          instagramUrl: user.instagramUrl,
          esFumador: user.esFumador,
          tieneMascota: user.tieneMascota,
          tienePareja: user.tienePareja,
          perfilLgtbi: user.perfilLgtbi
        });
      },
      error: (err) => this.mostrarMensaje('Error al cargar perfil')
    });
  }

  guardarPerfil() {
    if (this.perfilForm.invalid) return;
    
    const data: PerfilUpdateRequest = this.perfilForm.value;
    this.usuarioService.updateMyProfile(data).subscribe({
      next: (user) => {
        this.usuario.set(user);
        this.mostrarMensaje('Perfil actualizado correctamente');
      },
      error: (err) => this.mostrarMensaje('Error al actualizar perfil')
    });
  }

  cambiarPassword() {
    if (this.passwordForm.invalid) return;

    const data: CambiarPasswordRequest = this.passwordForm.value;
    this.usuarioService.changePassword(data).subscribe({
      next: () => {
        this.mostrarMensaje('Contraseña cambiada exitosamente');
        this.passwordForm.reset();
      },
      error: (err) => {
        const errorMsg = typeof err.error === 'string' ? err.error : 'Error al cambiar contraseña';
        this.mostrarMensaje(errorMsg);
      }
    });
  }

  private mostrarMensaje(msg: string) {
    this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
  }
}
