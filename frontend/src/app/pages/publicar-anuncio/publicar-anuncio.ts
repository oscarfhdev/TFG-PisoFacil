import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AnuncioService } from '../../services/anuncio.service';
import { FotoService } from '../../services/foto.service';
import { PublicarAnuncioRequest } from '../../models/anuncio.model';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-publicar-anuncio',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './publicar-anuncio.html',
  styleUrl: './publicar-anuncio.scss',
})
export class PublicarAnuncio {
  private fb = inject(FormBuilder);
  private anuncioService = inject(AnuncioService);
  private fotoService = inject(FotoService);
  private router = inject(Router);

  currentStep = signal(0);
  loading = signal(false);
  error = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  fotosPisoFiles = signal<File[]>([]);
  fotosHabitacionFiles = signal<File[]>([]);
  fotosPisoPreview = signal<string[]>([]);
  fotosHabitacionPreview = signal<string[]>([]);

  pisoForm: FormGroup = this.fb.group({
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
    lgtbiFriendly: [false],
    centroInteres: ['']
  });

  habitacionForm: FormGroup = this.fb.group({
    tituloAnuncioHabitacion: ['', Validators.required],
    precioMensualHabitacion: [null, [Validators.required, Validators.min(1)]],
    descripcionEspecificaHabitacion: [''],
    superficieM2Habitacion: [null, [Validators.min(1)]],
    tieneBanoPrivadoHabitacion: [false],
    amuebladaHabitacion: [false],
    exteriorHabitacion: [false],
    tieneCalefaccionHabitacion: [false],
    tieneAireAcondicionadoHabitacion: [false]
  });

  nextStep() {
    if (this.currentStep() === 0) {
      if (this.pisoForm.valid) {
        this.currentStep.set(1);
      } else {
        this.pisoForm.markAllAsTouched();
      }
    } else if (this.currentStep() === 1) {
      if (this.habitacionForm.valid) {
        this.currentStep.set(2);
      } else {
        this.habitacionForm.markAllAsTouched();
      }
    } else if (this.currentStep() === 2) {
      this.currentStep.set(3);
    }
  }

  prevStep() {
    if (this.currentStep() > 0) {
      this.currentStep.update(s => s - 1);
    }
  }

  onFilesSelectedPiso(event: Event) {
    const files = Array.from((event.target as HTMLInputElement).files || []);
    if (files.length > 0) {
      this.fotosPisoFiles.update(current => [...current, ...files]);
      
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = () => {
          this.fotosPisoPreview.update(current => [...current, reader.result as string]);
        };
        reader.readAsDataURL(file);
      });
    }
  }

  removeFotoPiso(index: number) {
    this.fotosPisoFiles.update(files => files.filter((_, i) => i !== index));
    this.fotosPisoPreview.update(previews => previews.filter((_, i) => i !== index));
  }

  onFilesSelectedHabitacion(event: Event) {
    const files = Array.from((event.target as HTMLInputElement).files || []);
    if (files.length > 0) {
      this.fotosHabitacionFiles.update(current => [...current, ...files]);
      
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = () => {
          this.fotosHabitacionPreview.update(current => [...current, reader.result as string]);
        };
        reader.readAsDataURL(file);
      });
    }
  }

  removeFotoHabitacion(index: number) {
    this.fotosHabitacionFiles.update(files => files.filter((_, i) => i !== index));
    this.fotosHabitacionPreview.update(previews => previews.filter((_, i) => i !== index));
  }

  onSubmit() {
    if (this.pisoForm.invalid || this.habitacionForm.invalid) {
      this.pisoForm.markAllAsTouched();
      this.habitacionForm.markAllAsTouched();
      this.error.set('Por favor, completa todos los campos requeridos correctamente.');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    const request: PublicarAnuncioRequest = {
      ...this.pisoForm.value,
      ...this.habitacionForm.value
    };

    this.anuncioService.publicar(request).subscribe({
      next: (res) => {
        const idPiso = res.piso.idPiso;
        const idHabitacion = res.habitacion.idHabitacion;
        
        this.successMessage.set('Anuncio creado. Subiendo fotos...');

        // Preparar observables de subida de fotos
        const uploadTasks: any[] = [];

        // Fotos del piso (sin idHabitacion)
        this.fotosPisoFiles().forEach(file => {
          uploadTasks.push(
            this.fotoService.upload(file, idPiso).pipe(
              catchError(err => {
                console.error('Error subiendo foto de piso', err);
                return of(null);
              })
            )
          );
        });

        // Fotos de la habitación (con idHabitacion)
        this.fotosHabitacionFiles().forEach(file => {
          uploadTasks.push(
            this.fotoService.upload(file, idPiso, idHabitacion).pipe(
              catchError(err => {
                console.error('Error subiendo foto de habitación', err);
                return of(null);
              })
            )
          );
        });

        if (uploadTasks.length > 0) {
          forkJoin(uploadTasks).subscribe({
            next: () => {
              this.successMessage.set(res.mensaje || '¡Anuncio publicado con éxito!');
              setTimeout(() => this.router.navigate(['/mis-anuncios']), 1500);
            },
            error: () => {
              this.error.set('El anuncio se creó, pero hubo un problema al subir algunas fotos.');
              this.loading.set(false);
              setTimeout(() => this.router.navigate(['/mis-anuncios']), 2000);
            }
          });
        } else {
          this.successMessage.set(res.mensaje || '¡Anuncio publicado con éxito!');
          setTimeout(() => this.router.navigate(['/mis-anuncios']), 1500);
        }
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Error al publicar el anuncio');
        this.loading.set(false);
      }
    });
  }
}
