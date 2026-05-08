import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { UsuarioResponse } from '../../models/usuario.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-usuario-modal',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatIconModule, MatChipsModule, DatePipe],
  template: `
    <div class="relative overflow-visible bg-white dark:bg-slate-900 text-texto dark:text-white rounded-3xl">
      <!-- Top Section (No banner colors) -->
      <div class="h-24 bg-slate-50 dark:bg-slate-800/50 rounded-t-3xl border-b border-gray-100 dark:border-slate-800/50">
        <button (click)="dialogRef.close()" 
                class="absolute top-4 right-4 w-10 h-10 flex items-center justify-center rounded-full bg-white/80 dark:bg-slate-800/80 text-gray-500 hover:text-primary transition-all shadow-sm z-10 focus:outline-none">
          <mat-icon class="!w-6 !h-6 text-[24px]">close</mat-icon>
        </button>
      </div>
      
      <!-- Contenido -->
      <div class="px-6 pb-8 pt-0 relative">
        <!-- Avatar Centered -->
        <div class="absolute -top-12 left-1/2 -translate-x-1/2">
          <div class="relative">
            <img [src]="usuario.fotoPerfilUrl || 'https://ui-avatars.com/api/?name=' + usuario.nombre + '&background=random'" 
                 (error)="$any($event.target).src='/placeholder.png'"
                 alt="Avatar" 
                 class="w-28 h-28 rounded-full border-4 border-white dark:border-slate-900 object-cover bg-white shadow-xl">
            <div class="absolute bottom-1 right-1 w-6 h-6 bg-green-500 border-2 border-white dark:border-slate-900 rounded-full flex items-center justify-center shadow-sm">
              <mat-icon class="!w-3 !h-3 text-[12px] text-white">check</mat-icon>
            </div>
          </div>
        </div>
        
        <div class="mt-20 text-center">
          <h2 class="text-2xl font-extrabold font-poppins mb-1">
            {{ usuario.nombre }} {{ usuario.apellidos }}
          </h2>
          <p class="text-slate-500 dark:text-slate-400 text-sm font-medium mb-6">
            Miembro desde {{ usuario.fechaRegistro | date:'mediumDate' }}
          </p>
          
          <!-- Info básica Grid -->
          <div class="grid grid-cols-2 gap-3 mb-8 text-left">
            @if (usuario.genero) {
              <div class="bg-slate-50 dark:bg-slate-800/50 p-3 rounded-2xl border border-gray-100 dark:border-slate-800">
                <span class="block text-[10px] text-slate-400 uppercase font-bold tracking-widest mb-1">Género</span>
                <span class="font-bold text-sm">{{ usuario.genero }}</span>
              </div>
            }
            @if (usuario.estudios) {
              <div class="bg-slate-50 dark:bg-slate-800/50 p-3 rounded-2xl border border-gray-100 dark:border-slate-800">
                <span class="block text-[10px] text-slate-400 uppercase font-bold tracking-widest mb-1">Estudios</span>
                <span class="font-bold text-sm truncate block">{{ usuario.estudios }}</span>
              </div>
            }
            @if (usuario.fechaNacimiento) {
              <div class="bg-slate-50 dark:bg-slate-800/50 p-3 rounded-2xl border border-gray-100 dark:border-slate-800">
                <span class="block text-[10px] text-slate-400 uppercase font-bold tracking-widest mb-1">Nacimiento</span>
                <span class="font-bold text-sm">{{ usuario.fechaNacimiento | date:'mediumDate' }}</span>
              </div>
            }
          </div>
          
          <!-- Biografía -->
          @if (usuario.biografia) {
            <div class="mb-8 text-left">
              <h3 class="text-xs text-slate-400 uppercase font-bold tracking-widest mb-3">Sobre mí</h3>
              <p class="text-sm text-slate-600 dark:text-slate-300 leading-relaxed bg-slate-50 dark:bg-slate-800/30 p-4 rounded-2xl italic border-l-4 border-primary/30">
                "{{ usuario.biografia }}"
              </p>
            </div>
          }
          
          <!-- Preferencias -->
          <div class="text-left">
            <h3 class="text-xs text-slate-400 uppercase font-bold tracking-widest mb-3">Estilo de vida</h3>
            <div class="flex flex-wrap gap-2">
              <span [class]="usuario.esFumador ? 'bg-red-50 text-red-600 dark:bg-red-900/20 dark:text-red-400' : 'bg-green-50 text-green-600 dark:bg-green-900/20 dark:text-green-400'" 
                    class="px-3 py-1.5 rounded-full text-xs font-bold flex items-center gap-1.5 border border-current/10">
                <mat-icon class="!w-4 !h-4 text-[16px]">{{ usuario.esFumador ? 'smoking_rooms' : 'no_smoking' }}</mat-icon>
                {{ usuario.esFumador ? 'Fumador' : 'No fumador' }}
              </span>
              
              @if (usuario.tieneMascota) {
                <span class="px-3 py-1.5 bg-amber-50 text-amber-600 dark:bg-amber-900/20 dark:text-amber-400 rounded-full text-xs font-bold flex items-center gap-1.5 border border-current/10">
                  <mat-icon class="!w-4 !h-4 text-[16px]">pets</mat-icon> Tiene mascota
                </span>
              }
              
              @if (usuario.tienePareja) {
                <span class="px-3 py-1.5 bg-blue-50 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400 rounded-full text-xs font-bold flex items-center gap-1.5 border border-current/10">
                  <mat-icon class="!w-4 !h-4 text-[16px]">favorite</mat-icon> En pareja
                </span>
              }
              
              @if (usuario.perfilLgtbi) {
                <span class="px-3 py-1.5 bg-purple-50 text-purple-600 dark:bg-purple-900/20 dark:text-purple-400 rounded-full text-xs font-bold flex items-center gap-1.5 border border-current/10">
                  <mat-icon class="!w-4 !h-4 text-[16px]">diversity_3</mat-icon> LGTBI+
                </span>
              }
            </div>
          </div>
          
          <!-- Botones Acción -->
          <div class="mt-10 flex gap-3">
            <button (click)="dialogRef.close()" 
                    class="w-full px-6 py-3.5 rounded-xl font-bold bg-slate-100 text-slate-500 hover:bg-slate-200 dark:bg-slate-800 dark:text-slate-300 dark:hover:bg-slate-700 transition-all focus:outline-none">
              Cerrar Perfil
            </button>
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
}
