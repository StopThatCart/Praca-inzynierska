/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { AuthenticationResponse } from '../models/authentication-response';
import { confirm } from '../fn/authentication/confirm';
import { Confirm$Params } from '../fn/authentication/confirm';
import { confirmus } from '../fn/authentication/confirmus';
import { Confirmus$Params } from '../fn/authentication/confirmus';
import { confirmus2 } from '../fn/authentication/confirmus-2';
import { Confirmus2$Params } from '../fn/authentication/confirmus-2';
import { login } from '../fn/authentication/login';
import { Login$Params } from '../fn/authentication/login';
import { refreshToken } from '../fn/authentication/refresh-token';
import { RefreshToken$Params } from '../fn/authentication/refresh-token';
import { register } from '../fn/authentication/register';
import { Register$Params } from '../fn/authentication/register';

@Injectable({ providedIn: 'root' })
export class AuthenticationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `register()` */
  static readonly RegisterPath = '/api/auth/register';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `register()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  register$Response(params: Register$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return register(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `register$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  register(params: Register$Params, context?: HttpContext): Observable<{
}> {
    return this.register$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `refreshToken()` */
  static readonly RefreshTokenPath = '/api/auth/refresh-token';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `refreshToken()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  refreshToken$Response(params: RefreshToken$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponse>> {
    return refreshToken(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `refreshToken$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  refreshToken(params: RefreshToken$Params, context?: HttpContext): Observable<AuthenticationResponse> {
    return this.refreshToken$Response(params, context).pipe(
      map((r: StrictHttpResponse<AuthenticationResponse>): AuthenticationResponse => r.body)
    );
  }

  /** Path part for operation `login()` */
  static readonly LoginPath = '/api/auth/login';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `login()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  login$Response(params: Login$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponse>> {
    return login(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `login$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  login(params: Login$Params, context?: HttpContext): Observable<AuthenticationResponse> {
    return this.login$Response(params, context).pipe(
      map((r: StrictHttpResponse<AuthenticationResponse>): AuthenticationResponse => r.body)
    );
  }

  /** Path part for operation `confirmus()` */
  static readonly ConfirmusPath = '/api/auth/test';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `confirmus()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirmus$Response(params?: Confirmus$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return confirmus(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `confirmus$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirmus(params?: Confirmus$Params, context?: HttpContext): Observable<string> {
    return this.confirmus$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `confirmus2()` */
  static readonly Confirmus2Path = '/api/auth/testUnprotected';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `confirmus2()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirmus2$Response(params?: Confirmus2$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return confirmus2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `confirmus2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirmus2(params?: Confirmus2$Params, context?: HttpContext): Observable<string> {
    return this.confirmus2$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `confirm()` */
  static readonly ConfirmPath = '/api/auth/aktywacja-konta';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `confirm()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirm$Response(params: Confirm$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return confirm(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `confirm$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirm(params: Confirm$Params, context?: HttpContext): Observable<void> {
    return this.confirm$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
