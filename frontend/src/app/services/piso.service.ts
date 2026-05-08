import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PisoRequest, PisoResponse } from '../models/piso.model';
import { MisPisosResponse } from '../models/mis-pisos.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PisoService {
  private http = inject(HttpClient);
  private apiUrl = '/api/pisos';

  findAll(): Observable<PisoResponse[]> {
    return this.http.get<PisoResponse[]>(this.apiUrl);
  }

  findAllAdmin(): Observable<MisPisosResponse[]> {
    return this.http.get<MisPisosResponse[]>(`${this.apiUrl}/admin-all`);
  }

  getMyPisos(): Observable<MisPisosResponse[]> {
    return this.http.get<MisPisosResponse[]>(`${this.apiUrl}/me`);
  }

  findById(id: number): Observable<PisoResponse> {
    return this.http.get<PisoResponse>(`${this.apiUrl}/${id}`);
  }

  create(data: PisoRequest): Observable<PisoResponse> {
    return this.http.post<PisoResponse>(this.apiUrl, data);
  }

  update(id: number, data: PisoRequest): Observable<PisoResponse> {
    return this.http.put<PisoResponse>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
