import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HabitacionRequest, HabitacionResponse, BusquedaFiltros } from '../models/habitacion.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HabitacionService {
  private http = inject(HttpClient);
  private apiUrl = '/api/habitaciones';

  findAll(): Observable<HabitacionResponse[]> {
    return this.http.get<HabitacionResponse[]>(this.apiUrl);
  }

  findById(id: number): Observable<HabitacionResponse> {
    return this.http.get<HabitacionResponse>(`${this.apiUrl}/${id}`);
  }

  findDisponibles(): Observable<HabitacionResponse[]> {
    return this.http.get<HabitacionResponse[]>(`${this.apiUrl}/disponibles`);
  }

  findDestacadas(): Observable<HabitacionResponse[]> {
    return this.http.get<HabitacionResponse[]>(`${this.apiUrl}/destacadas`);
  }

  buscarAvanzado(filtros: BusquedaFiltros): Observable<HabitacionResponse[]> {
    let params = new HttpParams();
    Object.entries(filtros).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, value.toString());
      }
    });
    return this.http.get<HabitacionResponse[]>(`${this.apiUrl}/buscar`, { params });
  }

  create(data: HabitacionRequest): Observable<HabitacionResponse> {
    return this.http.post<HabitacionResponse>(this.apiUrl, data);
  }

  update(id: number, data: HabitacionRequest): Observable<HabitacionResponse> {
    return this.http.put<HabitacionResponse>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
