import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { HabitacionDetail } from './pages/habitacion-detail/habitacion-detail';
import { PublicarAnuncio } from './pages/publicar-anuncio/publicar-anuncio';
import { MisFavoritos } from './pages/mis-favoritos/mis-favoritos';
import { MisAnuncios } from './pages/mis-anuncios/mis-anuncios';
import { Perfil } from './pages/perfil/perfil';
import { Buscar } from './pages/buscar/buscar';
import { Legal } from './pages/legal/legal';
import { NotFound } from './pages/not-found/not-found';
import { AdminLayout } from './admin/admin-layout/admin-layout';
import { AdminDashboard } from './admin/admin-dashboard/admin-dashboard';
import { AdminReportes } from './admin/admin-reportes/admin-reportes';
import { AdminUsuarios } from './admin/admin-usuarios/admin-usuarios';
import { AdminPisos } from './admin/admin-pisos/admin-pisos';
import { authGuard } from './auth/auth.guard';
import { adminGuard } from './auth/admin.guard';
import { noAuthGuard } from './auth/no-auth.guard';

export const routes: Routes = [
  // Rutas públicas
  { path: '', component: Home },
  { path: 'login', component: Login, canActivate: [noAuthGuard] },
  { path: 'register', component: Register, canActivate: [noAuthGuard] },
  { path: 'buscar', component: Buscar },
  { path: 'legal', component: Legal },
  { path: 'habitacion/:id', component: HabitacionDetail },
  
  // Rutas protegidas (usuario logueado)
  { path: 'publicar', component: PublicarAnuncio, canActivate: [authGuard] },
  { path: 'favoritos', component: MisFavoritos, canActivate: [authGuard] },
  { path: 'mis-anuncios', component: MisAnuncios, canActivate: [authGuard] },
  { path: 'perfil', component: Perfil, canActivate: [authGuard] },
  
  // Rutas de administración
  { 
    path: 'admin', 
    component: AdminLayout, 
    canActivate: [adminGuard],
    children: [
      { path: '', component: AdminDashboard },
      { path: 'usuarios', component: AdminUsuarios },
      { path: 'pisos', component: AdminPisos },
      { path: 'reportes', component: AdminReportes },
    ]
  },
  
  // Wildcard (404)
  { path: '**', component: NotFound }
];
