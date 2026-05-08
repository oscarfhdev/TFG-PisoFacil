import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HabitacionService } from '../../services/habitacion.service';
import { FotoService } from '../../services/foto.service';
import { FotoResponse } from '../../models/foto.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { concat, of } from 'rxjs';
import { catchError, toArray } from 'rxjs/operators';

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
  private fotoService = inject(FotoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  habitacionId = signal<number>(0);
  idPiso = signal<number>(0);
  loading = signal(false);
  loadingData = signal(true);
  error = signal<string | null>(null);

  // Fotos
  fotosExistentes = signal<FotoResponse[]>([]);
  nuevasFotosFiles = signal<File[]>([]);
  nuevasFotosPreview = signal<string[]>([]);
  eliminandoFoto = signal<number | null>(null);

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
        this.cargarFotos(id);
      },
      error: (err) => {
        console.error('Error al cargar la habitación', err);
        this.error.set('No se pudo cargar la habitación.');
        this.loadingData.set(false);
      }
    });
  }

  cargarFotos(idHabitacion: number) {
    this.fotoService.findByHabitacion(idHabitacion).subscribe({
      next: (fotos) => this.fotosExistentes.set(fotos),
      error: (err) => console.error('Error al cargar fotos de habitación', err)
    });
  }

  eliminarFoto(idFoto: number) {
    this.eliminandoFoto.set(idFoto);
    this.fotoService.delete(idFoto).subscribe({
      next: () => {
        this.fotosExistentes.update(fotos => fotos.filter(f => f.idFoto !== idFoto));
        this.eliminandoFoto.set(null);
      },
      error: () => {
        this.snackBar.open('Error al eliminar la foto.', 'Cerrar', { duration: 3000, panelClass: ['toast-error'] });
        this.eliminandoFoto.set(null);
      }
    });
  }

  onNuevasFotosSelected(event: Event) {
    const files = Array.from((event.target as HTMLInputElement).files || []);
    if (files.length > 0) {
      this.nuevasFotosFiles.update(c => [...c, ...files]);
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = () => this.nuevasFotosPreview.update(c => [...c, reader.result as string]);
        reader.readAsDataURL(file);
      });
    }
  }

  removeNuevaFoto(index: number) {
    this.nuevasFotosFiles.update(files => files.filter((_, i) => i !== index));
    this.nuevasFotosPreview.update(previews => previews.filter((_, i) => i !== index));
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
        const archivos = this.nuevasFotosFiles();
        if (archivos.length === 0) {
          this.snackBar.open('Habitación actualizada correctamente', 'Cerrar', { duration: 3000, panelClass: ['toast-success'] });
          this.router.navigate(['/mis-anuncios']);
          return;
        }

        const uploads = archivos.map(file =>
          this.fotoService.upload(file, this.idPiso(), this.habitacionId(), false).pipe(
            catchError(err => { console.error('Error subiendo foto', err); return of(null); })
          )
        );

        concat(...uploads).pipe(toArray()).subscribe({
          next: () => {
            this.snackBar.open('Habitación actualizada correctamente', 'Cerrar', { duration: 3000, panelClass: ['toast-success'] });
            this.router.navigate(['/mis-anuncios']);
          },
          error: () => {
            this.snackBar.open('Habitación actualizada, pero hubo un problema al subir las fotos.', 'Cerrar', { duration: 4000, panelClass: ['toast-warning'] });
            this.router.navigate(['/mis-anuncios']);
          }
        });
      },
      error: (err) => {
        console.error('Error al actualizar la habitación', err);
        this.error.set(err.error?.message || 'Error al actualizar la habitación. Inténtalo de nuevo.');
        this.loading.set(false);
      }
    });
  }
}
