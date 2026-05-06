import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from '../models/auth.model';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  
  currentUser = signal<LoginResponse | null>(null);
  isLoggedIn = signal<boolean>(false);
  isAdmin = signal<boolean>(false);

  constructor() {
    this.loadUserFromStorage();
  }

  private loadUserFromStorage() {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    if (token && userStr) {
      try {
        const user = JSON.parse(userStr) as LoginResponse;
        this.currentUser.set(user);
        this.isLoggedIn.set(true);
        this.isAdmin.set(user.role === 'ADMIN');
      } catch (e) {
        this.logout();
      }
    }
  }

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/api/auth/login', data).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('user', JSON.stringify(res));
        this.currentUser.set(res);
        this.isLoggedIn.set(true);
        this.isAdmin.set(res.role === 'ADMIN');
      })
    );
  }

  register(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>('/api/auth/register', data);
  }

  uploadProfilePhoto(userId: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`/api/usuarios/${userId}/foto-perfil`, formData);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUser.set(null);
    this.isLoggedIn.set(false);
    this.isAdmin.set(false);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
