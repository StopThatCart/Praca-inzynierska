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
import { login } from '../fn/authentication/login';
import { Login$Params } from '../fn/authentication/login';
import { refreshToken } from '../fn/authentication/refresh-token';
import { RefreshToken$Params } from '../fn/authentication/refresh-token';
import { register } from '../fn/authentication/register';
import { Register$Params } from '../fn/authentication/register';
import { zmianaEmail } from '../fn/authentication/zmiana-email';
import { ZmianaEmail$Params } from '../fn/authentication/zmiana-email';
import { zmianaHasla } from '../fn/authentication/zmiana-hasla';
import { ZmianaHasla$Params } from '../fn/authentication/zmiana-hasla';
import { zmianaHaslaEmail } from '../fn/authentication/zmiana-hasla-email';
import { ZmianaHaslaEmail$Params } from '../fn/authentication/zmiana-hasla-email';

@Injectable({ providedIn: 'root' })
export class AuthenticationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `zmianaHasla()` */
  static readonly ZmianaHaslaPath = '/api/auth/zmiana-hasla';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `zmianaHasla()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  zmianaHasla$Response(params: ZmianaHasla$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return zmianaHasla(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `zmianaHasla$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  zmianaHasla(params: ZmianaHasla$Params, context?: HttpContext): Observable<{
}> {
    return this.zmianaHasla$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `zmianaHaslaEmail()` */
  static readonly ZmianaHaslaEmailPath = '/api/auth/zmiana-hasla-email/{email}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `zmianaHaslaEmail()` instead.
   *
   * This method doesn't expect any request body.
   */
  zmianaHaslaEmail$Response(params: ZmianaHaslaEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return zmianaHaslaEmail(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `zmianaHaslaEmail$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  zmianaHaslaEmail(params: ZmianaHaslaEmail$Params, context?: HttpContext): Observable<{
}> {
    return this.zmianaHaslaEmail$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `zmianaEmail()` */
  static readonly ZmianaEmailPath = '/api/auth/zmiana-email';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `zmianaEmail()` instead.
   *
   * This method doesn't expect any request body.
   */
  zmianaEmail$Response(params: ZmianaEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return zmianaEmail(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `zmianaEmail$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  zmianaEmail(params: ZmianaEmail$Params, context?: HttpContext): Observable<{
}> {
    return this.zmianaEmail$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
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
   * This method doesn't expect any request body.
   */
  refreshToken$Response(params: RefreshToken$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponse>> {
    return refreshToken(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `refreshToken$Response()` instead.
   *
   * This method doesn't expect any request body.
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
