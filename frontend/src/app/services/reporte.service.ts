import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ReporteRequest, ReporteResponse } from '../models/reporte.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  private http = inject(HttpClient);
  private apiUrl = '/api/reportes';

  findAll(): Observable<ReporteResponse[]> {
    return this.http.get<ReporteResponse[]>(this.apiUrl);
  }

  findById(id: number): Observable<ReporteResponse> {
    return this.http.get<ReporteResponse>(`${this.apiUrl}/${id}`);
  }

  create(data: ReporteRequest): Observable<ReporteResponse> {
    return this.http.post<ReporteResponse>(this.apiUrl, data);
  }

  updateEstado(id: number, estado: string): Observable<ReporteResponse> {
    const params = new HttpParams().set('estado', estado);
    return this.http.put<ReporteResponse>(`${this.apiUrl}/${id}/estado`, {}, { params });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
