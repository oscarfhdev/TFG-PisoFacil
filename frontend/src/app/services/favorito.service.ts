import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FavoritoRequest, FavoritoResponse } from '../models/favorito.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FavoritoService {
  private http = inject(HttpClient);
  private apiUrl = '/api/favoritos';

  findByUsuario(idUsuario: number): Observable<FavoritoResponse[]> {
    return this.http.get<FavoritoResponse[]>(`${this.apiUrl}/usuario/${idUsuario}`);
  }

  checkFavorito(idUsuario: number, idHabitacion: number): Observable<boolean> {
    const params = new HttpParams()
      .set('idUsuario', idUsuario.toString())
      .set('idHabitacion', idHabitacion.toString());
    return this.http.get<boolean>(`${this.apiUrl}/check`, { params });
  }

  create(data: FavoritoRequest): Observable<FavoritoResponse> {
    return this.http.post<FavoritoResponse>(this.apiUrl, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
