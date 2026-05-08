import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AnuncioService } from '../../services/anuncio.service';
import { FotoService } from '../../services/foto.service';
import { PublicarAnuncioRequest } from '../../models/anuncio.model';
import { concat, of } from 'rxjs';
import { catchError, toArray } from 'rxjs/operators';

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

  // Índice de la foto marcada como principal del piso (0 por defecto = la primera)
  fotoPrincipalPisoIndex = signal<number>(0);

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
    // Recalcular índice principal
    const currentPrincipal = this.fotoPrincipalPisoIndex();
    if (index === currentPrincipal) {
      this.fotoPrincipalPisoIndex.set(0); // La nueva primera foto pasa a ser la principal
    } else if (index < currentPrincipal) {
      this.fotoPrincipalPisoIndex.update(v => v - 1);
    }
  }

  setPrincipalPiso(index: number) {
    this.fotoPrincipalPisoIndex.set(index);
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

        // Fotos del piso: enviar esPrincipal=true solo para la foto marcada con estrella
        const pisoFiles = this.fotosPisoFiles();
        const pisoIdx = this.fotoPrincipalPisoIndex();
        pisoFiles.forEach((file, index) => {
          const esPrincipal = index === pisoIdx;
          uploadTasks.push(
            this.fotoService.upload(file, idPiso, undefined, esPrincipal).pipe(
              catchError(err => {
                console.error('Error subiendo foto de piso', err);
                return of(null);
              })
            )
          );
        });

        // Fotos de la habitación: SIEMPRE esPrincipal=false
        // La foto principal del anuncio de habitación es siempre la del piso
        this.fotosHabitacionFiles().forEach((file) => {
          uploadTasks.push(
            this.fotoService.upload(file, idPiso, idHabitacion, false).pipe(
              catchError(err => {
                console.error('Error subiendo foto de habitación', err);
                return of(null);
              })
            )
          );
        });

        if (uploadTasks.length > 0) {
          // IMPORTANTE: concat en vez de forkJoin para subir en secuencia.
          // La primera foto del array es la principal — debe llegar PRIMERO al backend.
          // forkJoin es paralelo (race condition), concat es secuencial (garantizado).
          concat(...uploadTasks).pipe(toArray()).subscribe({
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
