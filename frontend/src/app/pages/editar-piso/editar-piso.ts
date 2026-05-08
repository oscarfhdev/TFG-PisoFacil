import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PisoService } from '../../services/piso.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-editar-piso',
  standalone: true,
  imports: [ReactiveFormsModule, MatIconModule, RouterLink],
  templateUrl: './editar-piso.html',
  styleUrl: './editar-piso.scss'
})
export class EditarPiso implements OnInit {
  private fb = inject(FormBuilder);
  private pisoService = inject(PisoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  pisoId = signal<number>(0);
  loading = signal(false);
  loadingData = signal(true);
  error = signal<string | null>(null);

  form: FormGroup = this.fb.group({
    direccion: ['', Validators.required],
    ciudad: ['', Validators.required],
    codigoPostal: [''],
    numHabitacionesTotal: [null, [Validators.min(1)]],
    numBanos: [null, [Validators.min(1)]],
    planta: [null],
    superficieTotalM2: [null, [Validators.min(1)]],
    descripcionGlobal: [''],
    tieneWifi: [false],
    tieneAscensor: [false],
    admiteFumadores: [false],
    admiteMascotas: [false],
    admiteParejas: [false],
    lgtbiFriendly: [false]
  });

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.pisoId.set(id);
    this.cargarPiso(id);
  }

  cargarPiso(id: number) {
    this.pisoService.findById(id).subscribe({
      next: (piso) => {
        this.form.patchValue({
          direccion: piso.direccion,
          ciudad: piso.ciudad,
          codigoPostal: piso.codigoPostal || '',
          numHabitacionesTotal: piso.numHabitacionesTotal ?? null,
          numBanos: piso.numBanos ?? null,
          planta: piso.planta ?? null,
          superficieTotalM2: piso.superficieTotalM2 ?? null,
          descripcionGlobal: piso.descripcionGlobal || '',
          tieneWifi: piso.tieneWifi ?? false,
          tieneAscensor: piso.tieneAscensor ?? false,
          admiteFumadores: piso.admiteFumadores ?? false,
          admiteMascotas: piso.admiteMascotas ?? false,
          admiteParejas: piso.admiteParejas ?? false,
          lgtbiFriendly: piso.lgtbiFriendly ?? false
        });
        this.loadingData.set(false);
      },
      error: (err) => {
        console.error('Error al cargar el piso', err);
        this.error.set('No se pudo cargar el piso.');
        this.loadingData.set(false);
      }
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    const datos = { ...this.form.value, idUsuario: 0 }; // idUsuario lo gestiona el backend por seguridad

    this.pisoService.update(this.pisoId(), datos).subscribe({
      next: () => {
        this.snackBar.open('Piso actualizado correctamente', 'Cerrar', {
          duration: 3000,
          panelClass: ['toast-success']
        });
        this.router.navigate(['/mis-anuncios']);
      },
      error: (err) => {
        console.error('Error al actualizar el piso', err);
        this.error.set(err.error?.message || 'Error al actualizar el piso. Inténtalo de nuevo.');
        this.loading.set(false);
      }
    });
  }
}
