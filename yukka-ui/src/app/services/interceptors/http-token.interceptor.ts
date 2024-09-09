import { HttpHandlerFn, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { TokenService } from '../token/token.service';
import { AuthenticationService } from '../services';
import { JwtHelperService } from '@auth0/angular-jwt';

export const httpTokenInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const tokenService = inject(TokenService);
  const authService = inject(AuthenticationService);
  const token = tokenService.token;
  const jwtHelper = new JwtHelperService();


 // console.log('HttpTokenInterceptor token:', token);

  if (token) {
    if (jwtHelper.isTokenExpired(token)) {
      return authService.refreshToken({ body: token }).pipe(
        switchMap((response) => {
          if (response.token) {
            tokenService.token = response.token;
          } else {
            tokenService.clearToken();
            return next(req);
          }
          const authReq = req.clone({
            headers: new HttpHeaders({
              Authorization: 'Bearer ' + response.token
            })
          });
          return next(authReq);
        }),
        catchError((error) => {
          tokenService.clearToken();
          return throwError(error);
        })
      );
    }


    const authReq = req.clone({
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + token
      })
    });
    return next(authReq);
  }
  return next(req);
};
