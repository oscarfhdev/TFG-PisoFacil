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
    <div class="p-6 text-center dark:bg-card-dark dark:text-white">
      <mat-icon [ngClass]="data.iconColor" class="!w-16 !h-16 text-6xl mb-4">{{ data.icon }}</mat-icon>
      <h2 class="text-2xl font-bold mb-2">{{ data.title }}</h2>
      <p class="text-gray-600 dark:text-slate-400 mb-6">{{ data.message }}</p>
      <button mat-flat-button color="primary" class="w-full" (click)="dialogRef.close()">
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
