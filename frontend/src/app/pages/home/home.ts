import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { HabitacionService } from '../../services/habitacion.service';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  private habitacionService = inject(HabitacionService);

  habitacionesDestacadas = toSignal(
    this.habitacionService.findDisponibles().pipe(
      catchError(() => of([]))
    ), 
    { initialValue: [] }
  );
}
