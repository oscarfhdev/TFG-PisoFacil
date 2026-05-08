import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioResponse } from '../../models/usuario.model';
import { PerfilUpdateRequest, CambiarPasswordRequest } from '../../models/perfil-update.model';
import { AuthService } from '../../services/auth.service';
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
  private authService = inject(AuthService);
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
      genero: ['', Validators.required],
      estudios: ['', Validators.required],
      biografia: [''],
      telefono: [''],
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
    // Pre-cargar datos básicos desde el currentUser (login) mientras carga el backend
    const currentUser = this.authService.currentUser();
    if (currentUser && !this.usuario()) {
      this.usuario.set({
        idUsuario: currentUser.idUsuario,
        nombre: currentUser.nombre,
        email: currentUser.email,
        esAdmin: currentUser.role === 'ADMIN',
        fotoPerfilUrl: currentUser.fotoPerfilUrl,
        fechaRegistro: '',
        cuentaActiva: true
      });
    }

    this.usuarioService.getMyProfile().subscribe({
      next: (user) => {
        // Si el backend no devuelve foto pero el currentUser la tiene, mantenerla
        if (!user.fotoPerfilUrl && currentUser?.fotoPerfilUrl) {
          user = { ...user, fotoPerfilUrl: currentUser.fotoPerfilUrl };
        }
        this.usuario.set(user);
        this.perfilForm.patchValue({
          nombre: user.nombre,
          apellidos: user.apellidos,
          email: user.email,
          fechaNacimiento: user.fechaNacimiento,
          genero: user.genero,
          estudios: user.estudios,
          biografia: user.biografia,
          telefono: user.telefono,
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
    if (this.perfilForm.invalid) {
      this.perfilForm.markAllAsTouched();
      return;
    }
    
    // Incluir la foto actual para que el backend no la sobreescriba con null
    const data: PerfilUpdateRequest = {
      ...this.perfilForm.value,
      fotoPerfilUrl: this.usuario()?.fotoPerfilUrl
    };
    this.usuarioService.updateMyProfile(data).subscribe({
      next: (user) => {
        this.usuario.set(user);
        this.mostrarMensaje('Perfil actualizado correctamente');
      },
      error: (err) => this.mostrarMensaje('Error al actualizar perfil')
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      if (this.usuario()?.idUsuario) {
        this.usuarioService.uploadFotoPerfil(this.usuario()!.idUsuario, file).subscribe({
          next: (user) => {
            this.usuario.set(user);
            const currentUser = this.authService.currentUser();
            if (currentUser) {
              const updatedUser = { ...currentUser, fotoPerfilUrl: user.fotoPerfilUrl };
              this.authService.currentUser.set(updatedUser);
              localStorage.setItem('user', JSON.stringify(updatedUser));
            }
            this.mostrarMensaje('Foto de perfil actualizada', false);
          },
          error: () => this.mostrarMensaje('Error al subir la foto', true)
        });
      }
    }
  }

  cambiarPassword() {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    const data: CambiarPasswordRequest = this.passwordForm.value;
    this.usuarioService.changePassword(data).subscribe({
      next: () => {
        this.mostrarMensaje('Contraseña cambiada exitosamente', false);
        this.passwordForm.reset();
      },
      error: (err) => {
        const errorMsg = typeof err.error === 'string' ? err.error : 'Error al cambiar contraseña';
        this.mostrarMensaje(errorMsg, true);
      }
    });
  }

  private mostrarMensaje(msg: string, isError = false) {
    this.snackBar.open(msg, 'Cerrar', { 
      duration: 3000,
      panelClass: isError ? ['toast-error'] : ['toast-success']
    });
  }
}
