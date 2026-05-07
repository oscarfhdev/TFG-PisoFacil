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
    <div class="p-0 overflow-hidden bg-white dark:bg-card-dark text-texto dark:text-white">
      <!-- Cabecera -->
      <div class="relative h-32 bg-gradient-to-r from-primary to-accent">
        <button mat-icon-button (click)="dialogRef.close()" class="absolute top-2 right-2 text-white bg-black/20 hover:bg-black/40">
          <mat-icon>close</mat-icon>
        </button>
      </div>
      
      <!-- Contenido -->
      <div class="px-6 pb-6 pt-0 relative">
        <!-- Avatar -->
        <div class="absolute -top-16 left-6">
          <img [src]="usuario.fotoPerfilUrl || 'assets/default-avatar.png'" alt="Avatar" 
               class="w-32 h-32 rounded-full border-4 border-white dark:border-card-dark object-cover bg-white shadow-md">
        </div>
        
        <div class="mt-20">
          <h2 class="text-2xl font-bold flex items-center gap-2">
            {{ usuario.nombre }} {{ usuario.apellidos }}
          </h2>
          <p class="text-slate-500 dark:text-slate-400 text-sm mb-4">
            Miembro desde {{ usuario.fechaRegistro | date:'mediumDate' }}
          </p>
          
          <!-- Info básica -->
          <div class="grid grid-cols-2 gap-4 mb-6">
            @if (usuario.genero) {
              <div class="flex flex-col">
                <span class="text-xs text-slate-500 uppercase tracking-wider">Género</span>
                <span class="font-medium">{{ usuario.genero }}</span>
              </div>
            }
            @if (usuario.estudios) {
              <div class="flex flex-col">
                <span class="text-xs text-slate-500 uppercase tracking-wider">Estudios</span>
                <span class="font-medium">{{ usuario.estudios }}</span>
              </div>
            }
            @if (usuario.fechaNacimiento) {
              <div class="flex flex-col">
                <span class="text-xs text-slate-500 uppercase tracking-wider">Nacimiento</span>
                <span class="font-medium">{{ usuario.fechaNacimiento | date:'mediumDate' }}</span>
              </div>
            }
            @if (usuario.instagramUrl) {
              <div class="flex flex-col">
                <span class="text-xs text-slate-500 uppercase tracking-wider">Instagram</span>
                <a [href]="usuario.instagramUrl" target="_blank" class="text-primary hover:underline font-medium truncate">
                  Ver Perfil
                </a>
              </div>
            }
          </div>
          
          <!-- Biografía -->
          @if (usuario.biografia) {
            <div class="mb-6">
              <h3 class="text-sm text-slate-500 uppercase tracking-wider mb-2">Sobre mí</h3>
              <p class="text-sm bg-slate-50 dark:bg-slate-800 p-4 rounded-xl border border-slate-100 dark:border-slate-700">
                {{ usuario.biografia }}
              </p>
            </div>
          }
          
          <!-- Preferencias / Afinidad -->
          <div>
            <h3 class="text-sm text-slate-500 uppercase tracking-wider mb-2">Estilo de vida</h3>
            <div class="flex flex-wrap gap-2">
              @if (usuario.esFumador) {
                <span class="px-3 py-1 bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400 rounded-full text-xs font-medium flex items-center gap-1">
                  🚬 Fumador
                </span>
              } @else {
                <span class="px-3 py-1 bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400 rounded-full text-xs font-medium flex items-center gap-1">
                  🚭 No fumador
                </span>
              }
              
              @if (usuario.tieneMascota) {
                <span class="px-3 py-1 bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400 rounded-full text-xs font-medium flex items-center gap-1">
                  🐾 Tiene mascota
                </span>
              }
              
              @if (usuario.tienePareja) {
                <span class="px-3 py-1 bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400 rounded-full text-xs font-medium flex items-center gap-1">
                  💑 Tiene pareja
                </span>
              }
              
              @if (usuario.perfilLgtbi) {
                <span class="px-3 py-1 bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-400 rounded-full text-xs font-medium flex items-center gap-1">
                  🏳️‍🌈 LGTBI+
                </span>
              }
            </div>
          </div>
          
        </div>
      </div>
      
      <!-- Footer -->
      <div class="p-4 border-t border-slate-100 dark:border-slate-800 flex justify-end gap-2 bg-slate-50 dark:bg-transparent">
        <button mat-button (click)="dialogRef.close()">Cerrar</button>
        <button mat-flat-button color="primary">
          <mat-icon>chat</mat-icon> Contactar
        </button>
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
