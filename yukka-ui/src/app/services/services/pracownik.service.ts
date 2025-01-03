/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addPracownik } from '../fn/pracownik/add-pracownik';
import { AddPracownik$Params } from '../fn/pracownik/add-pracownik';
import { remove } from '../fn/pracownik/remove';
import { Remove$Params } from '../fn/pracownik/remove';
import { setBanUzytkownik } from '../fn/pracownik/set-ban-uzytkownik';
import { SetBanUzytkownik$Params } from '../fn/pracownik/set-ban-uzytkownik';
import { unbanUzytkownik } from '../fn/pracownik/unban-uzytkownik';
import { UnbanUzytkownik$Params } from '../fn/pracownik/unban-uzytkownik';

@Injectable({ providedIn: 'root' })
export class PracownikService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `addPracownik()` */
  static readonly AddPracownikPath = '/pracownicy';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addPracownik()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addPracownik$Response(params?: AddPracownik$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return addPracownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addPracownik$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addPracownik(params?: AddPracownik$Params, context?: HttpContext): Observable<{
}> {
    return this.addPracownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `unbanUzytkownik()` */
  static readonly UnbanUzytkownikPath = '/pracownicy/unban/{uzytkownik-nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `unbanUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  unbanUzytkownik$Response(params: UnbanUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return unbanUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `unbanUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  unbanUzytkownik(params: UnbanUzytkownik$Params, context?: HttpContext): Observable<boolean> {
    return this.unbanUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
    );
  }

  /** Path part for operation `setBanUzytkownik()` */
  static readonly SetBanUzytkownikPath = '/pracownicy/ban';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `setBanUzytkownik()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  setBanUzytkownik$Response(params?: SetBanUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return setBanUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `setBanUzytkownik$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  setBanUzytkownik(params?: SetBanUzytkownik$Params, context?: HttpContext): Observable<boolean> {
    return this.setBanUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
    );
  }

  /** Path part for operation `remove()` */
  static readonly RemovePath = '/pracownicy/{uzytkownik-nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `remove()` instead.
   *
   * This method doesn't expect any request body.
   */
  remove$Response(params: Remove$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return remove(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `remove$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  remove(params: Remove$Params, context?: HttpContext): Observable<string> {
    return this.remove$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
