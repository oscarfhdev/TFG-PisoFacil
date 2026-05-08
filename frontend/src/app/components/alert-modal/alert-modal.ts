import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgClass } from '@angular/common';

export interface AlertModalData {
  title: string;
  message: string;
  icon: string;
  iconColor: string;
}

@Component({
  selector: 'app-alert-modal',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatIconModule, NgClass],
  template: `
    <div class="p-8 text-center bg-white dark:bg-slate-900 rounded-3xl relative overflow-visible">
      <div class="mb-6 inline-flex items-center justify-center w-20 h-20 rounded-full bg-slate-50 dark:bg-slate-800 shadow-inner">
        <mat-icon [ngClass]="data.iconColor" class="!w-12 !h-12 text-[48px] block">{{ data.icon }}</mat-icon>
      </div>
      <h2 class="text-2xl font-extrabold font-poppins mb-3 text-texto dark:text-white">{{ data.title }}</h2>
      <p class="text-slate-500 dark:text-slate-400 mb-8 leading-relaxed">{{ data.message }}</p>
      <button (click)="dialogRef.close()" 
              class="w-full bg-primary hover:bg-primary/90 text-white font-bold py-4 rounded-xl transition-all shadow-lg shadow-primary/20 focus:outline-none">
        Entendido
      </button>
    </div>
  `
})
export class AlertModal {
  constructor(
    public dialogRef: MatDialogRef<AlertModal>,
    @Inject(MAT_DIALOG_DATA) public data: AlertModalData
  ) {}
}
