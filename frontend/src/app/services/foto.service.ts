import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FotoResponse } from '../models/foto.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FotoService {
  private http = inject(HttpClient);
  private apiUrl = '/api/fotos';

  findByPiso(idPiso: number): Observable<FotoResponse[]> {
    return this.http.get<FotoResponse[]>(`${this.apiUrl}/piso/${idPiso}`);
  }

  findByHabitacion(idHabitacion: number): Observable<FotoResponse[]> {
    return this.http.get<FotoResponse[]>(`${this.apiUrl}/habitacion/${idHabitacion}`);
  }

  upload(archivo: File, idPiso: number, idHabitacion?: number): Observable<FotoResponse> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    formData.append('idPiso', idPiso.toString());
    if (idHabitacion) {
      formData.append('idHabitacion', idHabitacion.toString());
    }
    return this.http.post<FotoResponse>(this.apiUrl, formData);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
