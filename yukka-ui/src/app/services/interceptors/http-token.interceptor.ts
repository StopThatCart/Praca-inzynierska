import { HttpHandlerFn, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { TokenService } from '../token/token.service';
import { AuthenticationService } from '../services';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';

export const httpTokenInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const tokenService = inject(TokenService);
  const authService = inject(AuthenticationService);
  const token = tokenService.token;
  const router = inject(Router);
  const jwtHelper = new JwtHelperService();


 // console.log('HttpTokenInterceptor token:', token);

  if (token) {
    if (jwtHelper.isTokenExpired(token)) {
      return authService.refreshToken({ body: token }).pipe(
        switchMap((response) => {
          if (response.token) {
            tokenService.token = response.token;
            req = req.clone({
              setHeaders: {
                Authorization: `Bearer ${response.token}`
              }
            });
          } else {
            tokenService.clearToken();
          }
          return next(req);
        })
      );
    } else {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
  }
  
  return next(req).pipe(
    catchError((error) => {
      if (error.status === 302) {
        tokenService.clearToken();
        router.navigate(['/login']);
      }
      return throwError(error);
    })
  );
};
