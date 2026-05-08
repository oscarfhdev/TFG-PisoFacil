import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { UsuarioResponse } from '../../models/usuario.model';

@Component({
  selector: 'app-contacto-modal',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <div class="relative bg-white dark:bg-slate-900 text-texto dark:text-white rounded-3xl p-8">
      <button (click)="dialogRef.close()" 
              class="absolute top-4 right-4 w-10 h-10 flex items-center justify-center rounded-full bg-slate-50 dark:bg-slate-800 text-gray-500 hover:text-primary transition-all focus:outline-none">
        <mat-icon>close</mat-icon>
      </button>

      <div class="text-center mb-8 mt-2">
        <h2 class="text-2xl font-extrabold font-poppins mb-2">Métodos de Contacto</h2>
        <p class="text-slate-500 dark:text-slate-400">Contacta con {{ usuario.nombre }} para preguntar por la habitación.</p>
      </div>

      <div class="space-y-4">
        @if (usuario.email) {
          <a [href]="'mailto:' + usuario.email" class="w-full bg-slate-50 dark:bg-slate-800 hover:bg-primary/5 dark:hover:bg-primary/10 border border-gray-100 dark:border-slate-700 p-4 rounded-2xl flex items-center gap-4 transition-colors group">
            <div class="w-12 h-12 bg-blue-100 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400 rounded-full flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
              <mat-icon>email</mat-icon>
            </div>
            <div class="text-left">
              <p class="text-xs font-bold text-gray-400 dark:text-slate-500 uppercase tracking-wider mb-1">Correo Electrónico</p>
              <p class="font-medium text-texto dark:text-slate-200 truncate">{{ usuario.email }}</p>
            </div>
          </a>
        }
        
        @if (usuario.instagramUrl) {
          <a [href]="usuario.instagramUrl" target="_blank" class="w-full bg-slate-50 dark:bg-slate-800 hover:bg-primary/5 dark:hover:bg-primary/10 border border-gray-100 dark:border-slate-700 p-4 rounded-2xl flex items-center gap-4 transition-colors group">
            <div class="w-12 h-12 bg-pink-100 text-pink-600 dark:bg-pink-900/30 dark:text-pink-400 rounded-full flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
              <mat-icon>photo_camera</mat-icon>
            </div>
            <div class="text-left">
              <p class="text-xs font-bold text-gray-400 dark:text-slate-500 uppercase tracking-wider mb-1">Instagram</p>
              <p class="font-medium text-texto dark:text-slate-200">Ver Perfil</p>
            </div>
          </a>
        }
      </div>
      
      <div class="mt-8 text-center bg-amber-50 dark:bg-amber-900/20 p-4 rounded-2xl">
        <p class="text-xs text-amber-700 dark:text-amber-400 font-medium">
          <mat-icon class="!w-4 !h-4 text-[16px] align-middle mr-1">info</mat-icon>
          Recuerda no enviar pagos por adelantado a través de transferencias bancarias o sin visitar el piso.
        </p>
      </div>
    </div>
  `
})
export class ContactoModal {
  constructor(
    public dialogRef: MatDialogRef<ContactoModal>,
    @Inject(MAT_DIALOG_DATA) public usuario: UsuarioResponse
  ) {}
}
