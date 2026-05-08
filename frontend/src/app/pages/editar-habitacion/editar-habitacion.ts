import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HabitacionService } from '../../services/habitacion.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-editar-habitacion',
  standalone: true,
  imports: [ReactiveFormsModule, MatIconModule, RouterLink],
  templateUrl: './editar-habitacion.html',
  styleUrl: './editar-habitacion.scss'
})
export class EditarHabitacion implements OnInit {
  private fb = inject(FormBuilder);
  private habitacionService = inject(HabitacionService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  habitacionId = signal<number>(0);
  idPiso = signal<number>(0);
  loading = signal(false);
  loadingData = signal(true);
  error = signal<string | null>(null);

  form: FormGroup = this.fb.group({
    tituloAnuncio: ['', Validators.required],
    precioMensual: [null, [Validators.required, Validators.min(1)]],
    descripcionEspecifica: [''],
    superficieM2: [null, [Validators.min(1)]],
    tieneBanoPrivado: [false],
    amueblada: [false],
    exterior: [false],
    tieneCalefaccion: [false],
    tieneAireAcondicionado: [false],
    estaDisponible: [true]
  });

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.habitacionId.set(id);
    this.cargarHabitacion(id);
  }

  cargarHabitacion(id: number) {
    this.habitacionService.findById(id).subscribe({
      next: (hab) => {
        this.idPiso.set(hab.idPiso);
        this.form.patchValue({
          tituloAnuncio: hab.tituloAnuncio,
          precioMensual: hab.precioMensual,
          descripcionEspecifica: hab.descripcionEspecifica || '',
          superficieM2: hab.superficieM2 ?? null,
          tieneBanoPrivado: hab.tieneBanoPrivado ?? false,
          amueblada: hab.amueblada ?? false,
          exterior: hab.exterior ?? false,
          tieneCalefaccion: hab.tieneCalefaccion ?? false,
          tieneAireAcondicionado: hab.tieneAireAcondicionado ?? false,
          estaDisponible: hab.estaDisponible
        });
        this.loadingData.set(false);
      },
      error: (err) => {
        console.error('Error al cargar la habitación', err);
        this.error.set('No se pudo cargar la habitación.');
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

    const datos = {
      ...this.form.value,
      idPiso: this.idPiso()
    };

    this.habitacionService.update(this.habitacionId(), datos).subscribe({
      next: () => {
        this.snackBar.open('Habitación actualizada correctamente', 'Cerrar', {
          duration: 3000,
          panelClass: ['toast-success']
        });
        this.router.navigate(['/mis-anuncios']);
      },
      error: (err) => {
        console.error('Error al actualizar la habitación', err);
        this.error.set(err.error?.message || 'Error al actualizar la habitación. Inténtalo de nuevo.');
        this.loading.set(false);
      }
    });
  }
}
