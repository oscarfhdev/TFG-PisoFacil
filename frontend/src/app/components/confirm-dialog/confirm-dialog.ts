import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

export interface ConfirmDialogData {
  title: string;
  message: string;
  /** Optional list of items that will also be deleted (cascading effects). */
  warnings?: string[];
  confirmText?: string;
  cancelText?: string;
  confirmColor?: 'primary' | 'accent' | 'warn';
}

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <div class="confirm-dialog-wrapper">
      <!-- Header -->
      <div class="confirm-dialog-header" [class.warn]="data.confirmColor === 'warn'">
        <div class="confirm-dialog-icon">
          @if (data.confirmColor === 'warn') {
            <mat-icon>delete_forever</mat-icon>
          } @else {
            <mat-icon>help_outline</mat-icon>
          }
        </div>
        <h2 class="confirm-dialog-title">{{ data.title }}</h2>
      </div>

      <!-- Body -->
      <div class="confirm-dialog-body">
        <p class="confirm-dialog-message">{{ data.message }}</p>

        @if (data.warnings && data.warnings.length > 0) {
          <div class="confirm-dialog-warnings">
            <p class="confirm-warnings-label">
              <mat-icon class="confirm-warnings-icon">info</mat-icon>
              Esta acción también eliminará:
            </p>
            <ul class="confirm-warnings-list">
              @for (warning of data.warnings; track warning) {
                <li class="confirm-warning-item">
                  <mat-icon class="confirm-warning-bullet">chevron_right</mat-icon>
                  {{ warning }}
                </li>
              }
            </ul>
          </div>
        }

        <p class="confirm-dialog-irreversible">⚠ Esta acción no se puede deshacer.</p>
      </div>

      <!-- Actions -->
      <div class="confirm-dialog-actions">
        <button mat-stroked-button (click)="dialogRef.close(false)" class="confirm-cancel-btn">
          {{ data.cancelText || 'Cancelar' }}
        </button>
        <button mat-flat-button
                [color]="data.confirmColor || 'primary'"
                (click)="dialogRef.close(true)"
                class="confirm-confirm-btn">
          <mat-icon>delete</mat-icon>
          {{ data.confirmText || 'Confirmar' }}
        </button>
      </div>
    </div>
  `,
  styles: [`
    .confirm-dialog-wrapper {
      font-family: 'Poppins', sans-serif;
      min-width: 380px;
      max-width: 480px;
    }

    .confirm-dialog-header {
      display: flex;
      align-items: center;
      gap: 14px;
      padding: 24px 24px 0;
    }

    .confirm-dialog-icon {
      width: 44px;
      height: 44px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: rgba(99, 102, 241, 0.12);
      color: #6366f1;
      flex-shrink: 0;
    }

    .confirm-dialog-header.warn .confirm-dialog-icon {
      background: rgba(185, 28, 28, 0.1);
      color: #b91c1c;
    }

    .confirm-dialog-title {
      font-family: 'Poppins', sans-serif;
      font-size: 1.125rem;
      font-weight: 700;
      margin: 0;
      color: inherit;
    }

    .confirm-dialog-body {
      padding: 16px 24px 8px;
    }

    .confirm-dialog-message {
      font-family: 'Poppins', sans-serif;
      color: #6b7280;
      font-size: 0.9rem;
      line-height: 1.6;
      margin: 0 0 16px;
    }

    :host-context(.dark) .confirm-dialog-message {
      color: #94a3b8;
    }

    .confirm-dialog-warnings {
      background: rgba(185, 28, 28, 0.05);
      border: 1px solid rgba(185, 28, 28, 0.18);
      border-radius: 10px;
      padding: 12px 14px;
      margin-bottom: 12px;
    }

    :host-context(.dark) .confirm-dialog-warnings {
      background: rgba(185, 28, 28, 0.08);
      border-color: rgba(185, 28, 28, 0.25);
    }

    .confirm-warnings-label {
      display: flex;
      align-items: center;
      gap: 6px;
      font-family: 'Poppins', sans-serif;
      font-size: 0.75rem;
      font-weight: 600;
      color: #b91c1c;
      margin: 0 0 8px;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }

    .confirm-warnings-icon {
      font-size: 14px !important;
      width: 14px !important;
      height: 14px !important;
    }

    .confirm-warnings-list {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .confirm-warning-item {
      display: flex;
      align-items: center;
      gap: 4px;
      font-family: 'Poppins', sans-serif;
      font-size: 0.85rem;
      color: #b91c1c;
      font-weight: 500;
    }

    :host-context(.dark) .confirm-warning-item {
      color: #fca5a5;
    }

    .confirm-warning-bullet {
      font-size: 16px !important;
      width: 16px !important;
      height: 16px !important;
      color: #b91c1c;
    }

    .confirm-dialog-irreversible {
      font-family: 'Poppins', sans-serif;
      font-size: 0.78rem;
      color: #9ca3af;
      margin: 0;
      font-style: italic;
    }

    :host-context(.dark) .confirm-dialog-irreversible {
      color: #64748b;
    }

    .confirm-dialog-actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      padding: 20px 24px 24px;
    }

    /* ── Botón Cancelar ── */
    .confirm-cancel-btn {
      font-family: 'Poppins', sans-serif !important;
      font-size: 0.875rem !important;
      font-weight: 500 !important;
      padding: 0 22px !important;
      height: 40px !important;
      border-radius: 8px !important;
      border-color: #d1d5db !important;
      color: #6b7280 !important;
      letter-spacing: 0 !important;
      transition: background 0.18s, border-color 0.18s !important;
    }

    .confirm-cancel-btn:hover {
      background: rgba(0, 0, 0, 0.04) !important;
      border-color: #9ca3af !important;
    }

    :host-context(.dark) .confirm-cancel-btn {
      border-color: #374151 !important;
      color: #9ca3af !important;
    }

    :host-context(.dark) .confirm-cancel-btn:hover {
      background: rgba(255, 255, 255, 0.05) !important;
      border-color: #4b5563 !important;
    }

    /* ── Botón Confirmar (rojo) ── */
    .confirm-confirm-btn {
      font-family: 'Poppins', sans-serif !important;
      font-size: 0.875rem !important;
      font-weight: 600 !important;
      padding: 0 22px !important;
      height: 40px !important;
      border-radius: 8px !important;
      background: #b91c1c !important;
      color: #ffffff !important;
      letter-spacing: 0 !important;
      display: flex !important;
      align-items: center !important;
      gap: 6px !important;
      box-shadow: 0 2px 8px rgba(185, 28, 28, 0.3) !important;
      transition: background 0.18s, box-shadow 0.18s !important;
    }

    .confirm-confirm-btn:hover {
      background: #991b1b !important;
      box-shadow: 0 4px 12px rgba(185, 28, 28, 0.4) !important;
    }

    .confirm-confirm-btn mat-icon {
      font-size: 18px !important;
      width: 18px !important;
      height: 18px !important;
    }
  `]
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}
}

