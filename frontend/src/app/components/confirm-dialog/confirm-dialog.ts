import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

export interface ConfirmDialogData {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  confirmColor?: 'primary' | 'accent' | 'warn';
}

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <div class="p-6 dark:bg-card-dark dark:text-white">
      <h2 class="text-xl font-bold mb-4 flex items-center gap-2">
        @if (data.confirmColor === 'warn') {
          <mat-icon class="text-red-500">warning</mat-icon>
        } @else {
          <mat-icon class="text-primary">help_outline</mat-icon>
        }
        {{ data.title }}
      </h2>
      <p class="text-gray-600 dark:text-slate-400 mb-8 text-base leading-relaxed">
        {{ data.message }}
      </p>
      <div class="flex justify-end gap-3">
        <button mat-stroked-button (click)="dialogRef.close(false)" class="!px-6">
          {{ data.cancelText || 'Cancelar' }}
        </button>
        <button mat-flat-button [color]="data.confirmColor || 'primary'" (click)="dialogRef.close(true)" class="!px-6">
          {{ data.confirmText || 'Confirmar' }}
        </button>
      </div>
    </div>
  `
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}
}
