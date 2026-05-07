import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioResponse } from '../../models/usuario.model';

@Component({
  selector: 'app-admin-usuario-edit-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule
  ],
  templateUrl: './admin-usuario-edit-modal.html',
  styleUrl: './admin-usuario-edit-modal.scss'
})
export class AdminUsuarioEditModal implements OnInit {
  public dialogRef = inject(MatDialogRef<AdminUsuarioEditModal>);
  public data: { usuario: UsuarioResponse } = inject(MAT_DIALOG_DATA);
  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);
  private snackBar = inject(MatSnackBar);

  editForm!: FormGroup;
  resetPasswordForm!: FormGroup;
  
  loadingEdit = false;
  loadingReset = false;

  ngOnInit(): void {
    this.editForm = this.fb.group({
      nombre: [this.data.usuario.nombre, [Validators.required]],
      apellidos: [this.data.usuario.apellidos],
      email: [this.data.usuario.email, [Validators.required, Validators.email]],
      biografia: [this.data.usuario.biografia],
      fechaNacimiento: [this.data.usuario.fechaNacimiento],
      genero: [this.data.usuario.genero],
      estudios: [this.data.usuario.estudios],
      instagramUrl: [this.data.usuario.instagramUrl],
      esFumador: [this.data.usuario.esFumador || false],
      tieneMascota: [this.data.usuario.tieneMascota || false],
      tienePareja: [this.data.usuario.tienePareja || false],
      perfilLgtbi: [this.data.usuario.perfilLgtbi || false]
    });

    this.resetPasswordForm = this.fb.group({
      passwordNueva: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSaveDetails() {
    if (this.editForm.invalid) return;

    this.loadingEdit = true;
    const formValue = this.editForm.value;
    
    const updateData = {
      ...this.data.usuario,
      nombre: formValue.nombre,
      apellidos: formValue.apellidos,
      email: formValue.email,
      biografia: formValue.biografia,
      fechaNacimiento: formValue.fechaNacimiento,
      genero: formValue.genero,
      estudios: formValue.estudios,
      instagramUrl: formValue.instagramUrl,
      esFumador: formValue.esFumador,
      tieneMascota: formValue.tieneMascota,
      tienePareja: formValue.tienePareja,
      perfilLgtbi: formValue.perfilLgtbi
    };

    this.usuarioService.adminUpdateUser(this.data.usuario.idUsuario, updateData).subscribe({
      next: (res) => {
        this.snackBar.open('Detalles del usuario actualizados', 'OK', { duration: 3000 });
        this.data.usuario = res; // Update local data
        this.loadingEdit = false;
        this.dialogRef.close({ updatedUser: res });
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Error al actualizar el usuario', 'Cerrar', { duration: 3000 });
        this.loadingEdit = false;
      }
    });
  }

  onResetPassword() {
    if (this.resetPasswordForm.invalid) return;

    this.loadingReset = true;
    const passwordNueva = this.resetPasswordForm.get('passwordNueva')?.value;

    this.usuarioService.adminResetPassword(this.data.usuario.idUsuario, passwordNueva).subscribe({
      next: () => {
        this.snackBar.open('Contraseña reseteada correctamente', 'OK', { duration: 3000 });
        this.loadingReset = false;
        this.resetPasswordForm.reset();
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Error al resetear la contraseña', 'Cerrar', { duration: 3000 });
        this.loadingReset = false;
      }
    });
  }

  close() {
    this.dialogRef.close();
  }
}
