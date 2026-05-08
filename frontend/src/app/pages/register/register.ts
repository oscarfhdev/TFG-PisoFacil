import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/auth.model';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, MatSnackBarModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  currentStep = signal(0);
  loading = signal(false);
  error = signal<string | null>(null);
  
  fotoFile = signal<File | null>(null);
  previewUrl = signal<string | null>(null);

  // Formularios para cada paso
  step0Form: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]]
  }, { validators: this.passwordMatchValidator });

  step1Form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    apellidos: [''],
    fechaNacimiento: [''],
    estudios: ['']
  });

  step2Form: FormGroup = this.fb.group({
    biografia: [''],
    esFumador: [false],
    tieneMascota: [false],
    tienePareja: [false],
    perfilLgtbi: [false]
  });

  passwordMatchValidator(g: FormGroup) {
    return g.get('password')?.value === g.get('confirmPassword')?.value
      ? null : { mismatch: true };
  }

  nextStep() {
    if (this.currentStep() === 0) {
      if (this.step0Form.valid) {
        this.currentStep.set(1);
      } else {
        this.step0Form.markAllAsTouched();
      }
    } else if (this.currentStep() === 1) {
      if (this.step1Form.valid) {
        this.currentStep.set(2);
      } else {
        this.step1Form.markAllAsTouched();
      }
    } else if (this.currentStep() === 2) {
      if (this.step2Form.valid) {
        this.currentStep.set(3);
      } else {
        this.step2Form.markAllAsTouched();
      }
    }
  }

  prevStep() {
    if (this.currentStep() > 0) {
      this.currentStep.update(s => s - 1);
    }
  }

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.fotoFile.set(file);
      const reader = new FileReader();
      reader.onload = () => this.previewUrl.set(reader.result as string);
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    if (this.step0Form.invalid || this.step1Form.invalid || this.step2Form.invalid) {
      this.step0Form.markAllAsTouched();
      this.step1Form.markAllAsTouched();
      this.step2Form.markAllAsTouched();
      this.error.set('Por favor, completa todos los campos requeridos correctamente.');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    const request: RegisterRequest = {
      ...this.step0Form.value,
      ...this.step1Form.value,
      ...this.step2Form.value,
    };
    // remove confirmPassword
    delete (request as any).confirmPassword;

    this.authService.register(request).subscribe({
      next: (res) => {
        if (this.fotoFile() && res.idUsuario) {
          // El endpoint de subida de foto requiere autenticación JWT.
          // Hacemos un auto-login silencioso para obtener el token antes de subir la foto.
          const email = this.step0Form.value.email;
          const password = this.step0Form.value.password;
          this.authService.login({ email, password }).subscribe({
            next: () => {
              this.authService.uploadProfilePhoto(res.idUsuario, this.fotoFile()!).subscribe({
                next: () => {
                  this.authService.logout();
                  this.mostrarExito('¡Cuenta creada con éxito! Ya puedes iniciar sesión.');
                },
                error: () => {
                  // La foto falló pero el registro fue correcto
                  this.authService.logout();
                  this.mostrarExito('¡Cuenta creada! No se pudo subir la foto de perfil.');
                }
              });
            },
            error: () => {
              this.mostrarExito('¡Cuenta creada con éxito! Ya puedes iniciar sesión.');
            }
          });
        } else {
          this.mostrarExito('¡Cuenta creada con éxito! Ya puedes iniciar sesión.');
        }
      },
      error: (err) => {
        this.error.set(err.error || 'Error al registrar el usuario');
        this.loading.set(false);
      }
    });
  }

  private mostrarExito(msg: string) {
    this.loading.set(false);
    this.snackBar.open(msg, 'Cerrar', {
      duration: 3500,
      panelClass: ['toast-success']
    });
    setTimeout(() => this.router.navigate(['/login']), 3500);
  }
}
