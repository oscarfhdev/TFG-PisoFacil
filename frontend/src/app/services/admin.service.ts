import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AdminStats } from '../models/admin-stats.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);
  private apiUrl = '/api/admin';

  getStats(): Observable<AdminStats> {
    return this.http.get<AdminStats>(`${this.apiUrl}/stats`);
  }
}
