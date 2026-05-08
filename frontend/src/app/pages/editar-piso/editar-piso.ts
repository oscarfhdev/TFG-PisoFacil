import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PisoService } from '../../services/piso.service';
import { FotoService } from '../../services/foto.service';
import { FotoResponse } from '../../models/foto.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { concat, of } from 'rxjs';
import { catchError, toArray } from 'rxjs/operators';

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
  private fotoService = inject(FotoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  pisoId = signal<number>(0);
  loading = signal(false);
  loadingData = signal(true);
  error = signal<string | null>(null);

  // Fotos
  fotosExistentes = signal<FotoResponse[]>([]);
  nuevasFotosFiles = signal<File[]>([]);
  nuevasFotosPreview = signal<string[]>([]);
  fotoPrincipalId = signal<number | null>(null); // id de foto existente marcada como principal
  nuevaFotoPrincipalIndex = signal<number | null>(null); // índice en nuevas fotos si la principal es nueva
  eliminandoFoto = signal<number | null>(null);

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
        this.cargarFotos(id);
      },
      error: (err) => {
        console.error('Error al cargar el piso', err);
        this.error.set('No se pudo cargar el piso.');
        this.loadingData.set(false);
      }
    });
  }

  cargarFotos(idPiso: number) {
    this.fotoService.findByPiso(idPiso).subscribe({
      next: (fotos) => {
        // Solo fotos del piso (sin habitación asociada)
        const fotosPiso = fotos.filter(f => !f.idHabitacion);
        this.fotosExistentes.set(fotosPiso);
        // Marcar la principal actual (esPrincipal si existe, si no la primera)
        const principal = fotosPiso.find(f => f.esPrincipal);
        this.fotoPrincipalId.set(principal?.idFoto ?? fotosPiso[0]?.idFoto ?? null);
      },
      error: (err) => console.error('Error al cargar fotos', err)
    });
  }

  eliminarFoto(idFoto: number) {
    this.eliminandoFoto.set(idFoto);
    this.fotoService.delete(idFoto).subscribe({
      next: () => {
        const restantes = this.fotosExistentes().filter(f => f.idFoto !== idFoto);
        this.fotosExistentes.set(restantes);
        // Si se eliminó la principal, asignar la siguiente
        if (this.fotoPrincipalId() === idFoto) {
          this.fotoPrincipalId.set(restantes[0]?.idFoto ?? null);
        }
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
    if (this.nuevaFotoPrincipalIndex() === index) {
      this.nuevaFotoPrincipalIndex.set(null);
    } else if ((this.nuevaFotoPrincipalIndex() ?? -1) > index) {
      this.nuevaFotoPrincipalIndex.update(v => (v ?? 0) - 1);
    }
  }

  setPrincipalExistente(idFoto: number) {
    this.fotoPrincipalId.set(idFoto);
    this.nuevaFotoPrincipalIndex.set(null);
  }

  setPrincipalNueva(index: number) {
    this.nuevaFotoPrincipalIndex.set(index);
    this.fotoPrincipalId.set(null);
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
        const archivos = this.nuevasFotosFiles();
        if (archivos.length === 0) {
          this.snackBar.open('Piso actualizado correctamente', 'Cerrar', { duration: 3000, panelClass: ['toast-success'] });
          this.router.navigate(['/mis-anuncios']);
          return;
        }

        const principalNuevaIdx = this.nuevaFotoPrincipalIndex();
        const uploads = archivos.map((file, i) => {
          const esPrincipal = principalNuevaIdx === i;
          return this.fotoService.upload(file, this.pisoId(), undefined, esPrincipal).pipe(
            catchError(err => { console.error('Error subiendo foto', err); return of(null); })
          );
        });

        concat(...uploads).pipe(toArray()).subscribe({
          next: () => {
            this.snackBar.open('Piso actualizado correctamente', 'Cerrar', { duration: 3000, panelClass: ['toast-success'] });
            this.router.navigate(['/mis-anuncios']);
          },
          error: () => {
            this.snackBar.open('Piso actualizado, pero hubo un problema al subir las fotos.', 'Cerrar', { duration: 4000, panelClass: ['toast-warning'] });
            this.router.navigate(['/mis-anuncios']);
          }
        });
      },
      error: (err) => {
        console.error('Error al actualizar el piso', err);
        this.error.set(err.error?.message || 'Error al actualizar el piso. Inténtalo de nuevo.');
        this.loading.set(false);
      }
    });
  }
}
