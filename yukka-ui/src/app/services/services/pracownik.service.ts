/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { setBanUzytkownik } from '../fn/pracownik/set-ban-uzytkownik';
import { SetBanUzytkownik$Params } from '../fn/pracownik/set-ban-uzytkownik';

@Injectable({ providedIn: 'root' })
export class PracownikService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
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

}
