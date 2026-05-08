import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { UsuarioResponse } from '../../models/usuario.model';
import { DatePipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-usuario-modal',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatIconModule, MatChipsModule, DatePipe, NgClass],
  template: `
    <div class="bg-white dark:bg-slate-900 text-texto dark:text-white rounded-3xl overflow-hidden shadow-2xl flex flex-col relative">
      <!-- Top banner -->
      <div class="h-28 relative transition-colors duration-500" [ngClass]="getBannerColor()">
        <button (click)="dialogRef.close()" 
                class="absolute top-4 right-4 w-10 h-10 flex items-center justify-center rounded-full bg-black/10 hover:bg-black/20 dark:bg-white/20 dark:hover:bg-white/40 text-white transition-all focus:outline-none">
          <mat-icon>close</mat-icon>
        </button>
      </div>
      
      <!-- Contenido -->
      <div class="px-6 pb-8 -mt-14">
        <div class="flex flex-col items-center">
          <img [src]="usuario.fotoPerfilUrl || 'https://ui-avatars.com/api/?name=' + usuario.nombre + '&background=random'" 
               (error)="$any($event.target).src='/avatar-placeholder.png'"
               alt="Avatar" 
               class="w-28 h-28 rounded-full border-4 border-white dark:border-slate-900 object-cover bg-white shadow-md z-10">
          
          <h2 class="text-2xl font-bold font-poppins mt-3 mb-1 text-center">
            {{ usuario.nombre }} {{ usuario.apellidos }}
          </h2>
          <p class="text-slate-500 dark:text-slate-400 text-sm mb-6">
            Miembro desde {{ usuario.fechaRegistro | date:'mediumDate' }}
          </p>
        </div>
        
        <!-- Info básica Grid -->
        <div class="grid grid-cols-2 gap-3 mb-6">
          @if (usuario.genero) {
            <div class="bg-slate-50 dark:bg-slate-800/50 p-3 rounded-xl border border-gray-100 dark:border-slate-800 flex items-center gap-3">
              <mat-icon class="text-gray-400">wc</mat-icon>
              <div class="overflow-hidden">
                <span class="block text-[10px] text-slate-400 uppercase font-semibold tracking-widest mb-0.5">Género</span>
                <span class="text-sm truncate block">{{ usuario.genero }}</span>
              </div>
            </div>
          }
          @if (usuario.estudios) {
            <div class="bg-slate-50 dark:bg-slate-800/50 p-3 rounded-xl border border-gray-100 dark:border-slate-800 flex items-center gap-3">
              <mat-icon class="text-gray-400">school</mat-icon>
              <div class="overflow-hidden">
                <span class="block text-[10px] text-slate-400 uppercase font-semibold tracking-widest mb-0.5">Estudios</span>
                <span class="text-sm truncate block" [title]="usuario.estudios">{{ usuario.estudios }}</span>
              </div>
            </div>
          }
          @if (usuario.fechaNacimiento) {
            <div class="bg-slate-50 dark:bg-slate-800/50 p-3 rounded-xl border border-gray-100 dark:border-slate-800 flex items-center gap-3">
              <mat-icon class="text-gray-400">cake</mat-icon>
              <div class="overflow-hidden">
                <span class="block text-[10px] text-slate-400 uppercase font-semibold tracking-widest mb-0.5">Edad</span>
                <span class="text-sm block">{{ getEdad() }} años</span>
              </div>
            </div>
          }
        </div>
        
        <!-- Biografía -->
        @if (usuario.biografia) {
          <div class="mb-6">
            <h3 class="text-xs text-slate-400 uppercase font-semibold tracking-widest mb-2">Sobre mí</h3>
            <p class="text-sm text-slate-600 dark:text-slate-300 leading-relaxed bg-slate-50 dark:bg-slate-800/30 p-4 rounded-xl border border-gray-100 dark:border-slate-800 italic">
              "{{ usuario.biografia }}"
            </p>
          </div>
        }
        
        <!-- Preferencias -->
        <div>
          <h3 class="text-xs text-slate-400 uppercase font-semibold tracking-widest mb-2">Estilo de vida</h3>
          <div class="flex flex-wrap gap-2">
            <span [class]="usuario.esFumador ? 'bg-red-50 text-red-600 dark:bg-red-900/20 dark:text-red-400' : 'bg-green-50 text-green-600 dark:bg-green-900/20 dark:text-green-400'" 
                  class="px-3 py-1.5 rounded-full text-sm flex items-center gap-1.5 border border-current/10">
              <mat-icon class="!w-4 !h-4 text-[16px]">{{ usuario.esFumador ? 'smoking_rooms' : 'smoke_free' }}</mat-icon>
              {{ usuario.esFumador ? 'Fumador' : 'No fumador' }}
            </span>
            
            @if (usuario.tieneMascota) {
              <span class="px-3 py-1.5 bg-amber-50 text-amber-600 dark:bg-amber-900/20 dark:text-amber-400 rounded-full text-sm flex items-center gap-1.5 border border-current/10">
                <mat-icon class="!w-4 !h-4 text-[16px]">pets</mat-icon> Mascotas
              </span>
            }
            
            @if (usuario.tienePareja) {
              <span class="px-3 py-1.5 bg-blue-50 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400 rounded-full text-sm flex items-center gap-1.5 border border-current/10">
                <mat-icon class="!w-4 !h-4 text-[16px]">favorite</mat-icon> En pareja
              </span>
            }
            
            @if (usuario.perfilLgtbi) {
              <span class="px-3 py-1.5 bg-purple-50 text-purple-600 dark:bg-purple-900/20 dark:text-purple-400 rounded-full text-sm flex items-center gap-1.5 border border-current/10">
                <mat-icon class="!w-4 !h-4 text-[16px]">diversity_3</mat-icon> LGTBI+
              </span>
            }
          </div>
        </div>
        
      </div>
    </div>
  `
})
export class UsuarioModal {
  constructor(
    public dialogRef: MatDialogRef<UsuarioModal>,
    @Inject(MAT_DIALOG_DATA) public usuario: UsuarioResponse
  ) {}

  getBannerColor(): string {
    const gen = this.usuario.genero?.toLowerCase() || '';
    if (gen.includes('femenino') || gen.includes('mujer')) {
      return 'bg-pink-300 dark:bg-pink-800';
    } else if (gen.includes('masculino') || gen.includes('hombre')) {
      return 'bg-primary';
    } else {
      return 'bg-accent dark:bg-orange-600';
    }
  }

  getEdad(): number {
    if (!this.usuario.fechaNacimiento) return 0;
    const hoy = new Date();
    const nacimiento = new Date(this.usuario.fechaNacimiento);
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
      edad--;
    }
    return edad;
  }
}
