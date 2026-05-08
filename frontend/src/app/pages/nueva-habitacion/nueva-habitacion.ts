import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HabitacionService } from '../../services/habitacion.service';
import { FotoService } from '../../services/foto.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { concat, of } from 'rxjs';
import { catchError, toArray } from 'rxjs/operators';

@Component({
  selector: 'app-nueva-habitacion',
  standalone: true,
  imports: [ReactiveFormsModule, MatIconModule, RouterLink],
  templateUrl: './nueva-habitacion.html',
  styleUrl: './nueva-habitacion.scss'
})
export class NuevaHabitacion implements OnInit {
  private fb = inject(FormBuilder);
  private habitacionService = inject(HabitacionService);
  private fotoService = inject(FotoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  pisoId = signal<number>(0);
  loading = signal(false);
  error = signal<string | null>(null);

  fotosFiles = signal<File[]>([]);
  fotosPreview = signal<string[]>([]);
  fotoPrincipalIndex = signal<number>(0);

  form: FormGroup = this.fb.group({
    tituloAnuncio: ['', Validators.required],
    precioMensual: [null, [Validators.required, Validators.min(1)]],
    descripcionEspecifica: [''],
    superficieM2: [null, [Validators.min(1)]],
    tieneBanoPrivado: [false],
    amueblada: [false],
    exterior: [false],
    tieneCalefaccion: [false],
    tieneAireAcondicionado: [false]
  });

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('pisoId'));
    this.pisoId.set(id);
  }

  onFilesSelected(event: Event) {
    const files = Array.from((event.target as HTMLInputElement).files || []);
    if (files.length > 0) {
      this.fotosFiles.update(current => [...current, ...files]);
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = () => {
          this.fotosPreview.update(current => [...current, reader.result as string]);
        };
        reader.readAsDataURL(file);
      });
    }
  }

  removeFoto(index: number) {
    this.fotosFiles.update(files => files.filter((_, i) => i !== index));
    this.fotosPreview.update(previews => previews.filter((_, i) => i !== index));
    const currentPrincipal = this.fotoPrincipalIndex();
    if (index === currentPrincipal) {
      this.fotoPrincipalIndex.set(0);
    } else if (index < currentPrincipal) {
      this.fotoPrincipalIndex.update(v => v - 1);
    }
  }

  setPrincipal(index: number) {
    this.fotoPrincipalIndex.set(index);
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
      idPiso: this.pisoId(),
      estaDisponible: true
    };

    this.habitacionService.create(datos).subscribe({
      next: (hab) => {
        const idHabitacion = hab.idHabitacion;
        const idPiso = this.pisoId();

        // Reordenar fotos: la principal va primero
        const files = [...this.fotosFiles()];
        const idx = this.fotoPrincipalIndex();
        if (idx > 0 && idx < files.length) {
          const [principal] = files.splice(idx, 1);
          files.unshift(principal);
        }

        if (files.length > 0) {
          const uploads = files.map((file, index) => {
            // La foto que tenía la estrella se envía con esPrincipal=true
            const esPrincipal = index === this.fotoPrincipalIndex();
            return this.fotoService.upload(file, idPiso, idHabitacion, esPrincipal).pipe(
              catchError(err => {
                console.error('Error subiendo foto', err);
                return of(null);
              })
            );
          });

          // Subir en secuencia para garantizar que la foto principal llega primero al backend
          concat(...uploads).pipe(toArray()).subscribe({
            next: () => {
              this.snackBar.open('Habitación añadida correctamente', 'Cerrar', {
                duration: 3000,
                panelClass: ['toast-success']
              });
              this.router.navigate(['/mis-anuncios']);
            },
            error: () => {
              this.snackBar.open('Habitación creada, pero hubo un problema al subir las fotos.', 'Cerrar', {
                duration: 4000,
                panelClass: ['toast-warning']
              });
              this.router.navigate(['/mis-anuncios']);
            }
          });
        } else {
          this.snackBar.open('Habitación añadida correctamente', 'Cerrar', {
            duration: 3000,
            panelClass: ['toast-success']
          });
          this.router.navigate(['/mis-anuncios']);
        }
      },
      error: (err) => {
        console.error('Error al crear la habitación', err);
        this.error.set(err.error?.message || 'Error al crear la habitación. Inténtalo de nuevo.');
        this.loading.set(false);
      }
    });
  }
}
