import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { HabitacionDetail } from './pages/habitacion-detail/habitacion-detail';
import { PublicarAnuncio } from './pages/publicar-anuncio/publicar-anuncio';
import { MisFavoritos } from './pages/mis-favoritos/mis-favoritos';
import { Perfil } from './pages/perfil/perfil';
import { NotFound } from './pages/not-found/not-found';
import { AdminDashboard } from './admin/admin-dashboard/admin-dashboard';
import { AdminReportes } from './admin/admin-reportes/admin-reportes';
import { AdminUsuarios } from './admin/admin-usuarios/admin-usuarios';
import { authGuard } from './auth/auth.guard';
import { adminGuard } from './auth/admin.guard';

export const routes: Routes = [
  // Rutas públicas
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'habitacion/:id', component: HabitacionDetail },
  
  // Rutas protegidas (usuario logueado)
  { path: 'publicar', component: PublicarAnuncio, canActivate: [authGuard] },
  { path: 'favoritos', component: MisFavoritos, canActivate: [authGuard] },
  { path: 'perfil', component: Perfil, canActivate: [authGuard] },
  
  // Rutas de administración
  { path: 'admin', component: AdminDashboard, canActivate: [adminGuard] },
  { path: 'admin/reportes', component: AdminReportes, canActivate: [adminGuard] },
  { path: 'admin/usuarios', component: AdminUsuarios, canActivate: [adminGuard] },
  
  // Wildcard (404)
  { path: '**', component: NotFound }
];
