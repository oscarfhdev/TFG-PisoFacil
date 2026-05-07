import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FavoritoRequest, FavoritoResponse } from '../models/favorito.model';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FavoritoService {
  private http = inject(HttpClient);
  private apiUrl = '/api/favoritos';

  favoritosIds = signal<Set<number>>(new Set());

  getMisFavoritos(): Observable<FavoritoResponse[]> {
    return this.http.get<FavoritoResponse[]>(`${this.apiUrl}/me`).pipe(
      tap(favoritos => {
        const ids = new Set(favoritos.map(f => f.idHabitacion));
        this.favoritosIds.set(ids);
      })
    );
  }

  toggle(idHabitacion: number): Observable<{ esFavorito: boolean }> {
    return this.http.post<{ esFavorito: boolean }>(`${this.apiUrl}/toggle/${idHabitacion}`, {}).pipe(
      tap(res => {
        const currentIds = new Set(this.favoritosIds());
        if (res.esFavorito) {
          currentIds.add(idHabitacion);
        } else {
          currentIds.delete(idHabitacion);
        }
        this.favoritosIds.set(currentIds);
      })
    );
  }

  checkMiFavorito(idHabitacion: number): Observable<boolean> {
    const params = new HttpParams().set('idHabitacion', idHabitacion.toString());
    return this.http.get<boolean>(`${this.apiUrl}/check-auth`, { params }).pipe(
      tap(isFav => {
        const currentIds = new Set(this.favoritosIds());
        if (isFav) {
          currentIds.add(idHabitacion);
        } else {
          currentIds.delete(idHabitacion);
        }
        this.favoritosIds.set(currentIds);
      })
    );
  }

  // --- Endpoints antiguos mantenidos por si acaso ---
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
