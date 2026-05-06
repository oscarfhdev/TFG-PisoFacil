import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PublicarAnuncioRequest, PublicarAnuncioResponse } from '../models/anuncio.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService {
  private http = inject(HttpClient);
  private apiUrl = '/api/anuncios';

  publicar(data: PublicarAnuncioRequest): Observable<PublicarAnuncioResponse> {
    return this.http.post<PublicarAnuncioResponse>(`${this.apiUrl}/publicar`, data);
  }
}
