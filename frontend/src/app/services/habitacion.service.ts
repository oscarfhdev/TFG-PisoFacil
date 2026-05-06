import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HabitacionRequest, HabitacionResponse } from '../models/habitacion.model';
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

  buscar(ciudad: string, precioMax?: number): Observable<HabitacionResponse[]> {
    let params = new HttpParams().set('ciudad', ciudad);
    if (precioMax !== undefined) {
      params = params.set('precioMax', precioMax.toString());
    }
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
