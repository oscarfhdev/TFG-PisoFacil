import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ReporteService } from '../../services/reporte.service';

@Component({
  selector: 'app-crear-reporte-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule
  ],
  templateUrl: './crear-reporte-modal.html',
  styleUrl: './crear-reporte-modal.scss'
})
export class CrearReporteModal implements OnInit {
  public dialogRef = inject(MatDialogRef<CrearReporteModal>);
  private fb = inject(FormBuilder);
  private reporteService = inject(ReporteService);
  private snackBar = inject(MatSnackBar);
  private data = inject(MAT_DIALOG_DATA, { optional: true });

  reporteForm!: FormGroup;
  loading = false;

  categorias = [
    'Error en la web',
    'Sugerencia',
    'Anuncio fraudulento o inapropiado',
    'Otro'
  ];

  ngOnInit(): void {
    this.reporteForm = this.fb.group({
      categoria: [this.data?.categoria || '', [Validators.required]],
      mensaje: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onSubmit() {
    if (this.reporteForm.invalid) {
      this.reporteForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    const formValue = this.reporteForm.value;
    
    // Generamos el título automáticamente a partir de la categoría para no abrumar al usuario
    const reporteData = {
      categoria: formValue.categoria,
      titulo: formValue.categoria, 
      mensaje: formValue.mensaje
    };

    this.reporteService.create(reporteData).subscribe({
      next: () => {
        this.snackBar.open('¡Gracias por tu reporte! Lo revisaremos pronto.', 'OK', { duration: 5000, panelClass: ['toast-success'] });
        this.loading = false;
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Error al enviar el reporte. Inténtalo de nuevo.', 'Cerrar', { duration: 3000, panelClass: ['toast-error'] });
        this.loading = false;
      }
    });
  }

  close() {
    this.dialogRef.close();
  }
}
