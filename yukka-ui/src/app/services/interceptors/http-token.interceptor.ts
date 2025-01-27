import { HttpContextToken, HttpHandlerFn, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { TokenService } from '../token/token.service';
import { AuthenticationService } from '../services';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
import { refreshToken } from '../fn/authentication/refresh-token';


export const httpTokenInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const tokenService = inject(TokenService);
  const authService = inject(AuthenticationService);
  const token = tokenService.token;
  const refreshToken = tokenService.refreshToken;
  const router = inject(Router);
  const jwtHelper = new JwtHelperService();

  if (req.url.includes(AuthenticationService.RefreshTokenPath)) {
    return next(req);
  }

  if (refreshToken) {
    if (jwtHelper.isTokenExpired(refreshToken)) {
      tokenService.clearRefreshToken();
      tokenService.clearToken();
    } else if (!token || jwtHelper.isTokenExpired(token)) {
      return authService.refreshToken({ 'X-Refresh-Token': refreshToken }).pipe(
        switchMap((response) => {
          if (response.token && response.refreshToken) {
            tokenService.token = response.token as string;
            tokenService.refreshToken = response.refreshToken as string;

            req = req.clone({
              setHeaders: {
                Authorization: `Bearer ${response.token}`,
                'X-Refresh-Token': refreshToken
              }
            });
          } else {
            tokenService.clearRefreshToken();
          }
          return next(req);
        }),
        catchError((error) => {
          console.log('Błąd podczas odświeżania tokena:', error);
          return throwError(() => error);
        })
      );
    } else {
        req = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
            'X-Refresh-Token': refreshToken
          }
        });
    }
  }

  return next(req).pipe(
    catchError((error) => {
      // Kod 302 oznacza, że użytkownik jest zbanowany
      if (error.status === 302) {
        tokenService.clearToken();
        tokenService.clearRefreshToken();
        router.navigate(['/login']);
      }
      return throwError(error);
    })
  );
};
