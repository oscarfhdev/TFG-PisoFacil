import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);
  const token = authService.getToken();

  let clonedReq = req;
  if (token) {
    clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(clonedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      const isAuthEndpoint = req.url.includes('/auth/login') || req.url.includes('/auth/register');
      if ((error.status === 401 || error.status === 403) && !isAuthEndpoint) {
        authService.logout();
        snackBar.open('Tu sesión ha caducado. Vuelve a iniciar sesión', 'Cerrar', {
          duration: 5000,
          panelClass: ['toast-error'],
          horizontalPosition: 'center',
          verticalPosition: 'bottom'
        });
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
