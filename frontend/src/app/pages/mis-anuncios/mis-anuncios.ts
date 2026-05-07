import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PisoService } from '../../services/piso.service';
import { MisPisosResponse } from '../../models/mis-pisos.model';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-mis-anuncios',
  standalone: true,
  imports: [CommonModule, MatIconModule, RouterLink],
  templateUrl: './mis-anuncios.html',
  styleUrl: './mis-anuncios.scss'
})
export class MisAnuncios implements OnInit {
  private pisoService = inject(PisoService);
  
  pisos = signal<MisPisosResponse[]>([]);
  loading = signal<boolean>(true);

  ngOnInit() {
    this.cargarPisos();
  }

  cargarPisos() {
    this.pisoService.getMyPisos().subscribe({
      next: (data) => {
        this.pisos.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar mis pisos', err);
        this.loading.set(false);
      }
    });
  }
}
