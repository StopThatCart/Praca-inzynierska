/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { findAllRoslinyOfUzytkownik } from '../fn/uzytkownik-roslina/find-all-rosliny-of-uzytkownik';
import { FindAllRoslinyOfUzytkownik$Params } from '../fn/uzytkownik-roslina/find-all-rosliny-of-uzytkownik';
import { PageResponseRoslinaResponse } from '../models/page-response-roslina-response';
import { saveRoslina } from '../fn/uzytkownik-roslina/save-roslina';
import { SaveRoslina$Params } from '../fn/uzytkownik-roslina/save-roslina';
import { updateRoslina1 } from '../fn/uzytkownik-roslina/update-roslina-1';
import { UpdateRoslina1$Params } from '../fn/uzytkownik-roslina/update-roslina-1';
import { updateRoslinaObraz1 } from '../fn/uzytkownik-roslina/update-roslina-obraz-1';
import { UpdateRoslinaObraz1$Params } from '../fn/uzytkownik-roslina/update-roslina-obraz-1';

@Injectable({ providedIn: 'root' })
export class UzytkownikRoslinaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `saveRoslina()` */
  static readonly SaveRoslinaPath = '/uzytkownikRosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina$Response(params?: SaveRoslina$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return saveRoslina(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina(params?: SaveRoslina$Params, context?: HttpContext): Observable<{
}> {
    return this.saveRoslina$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `updateRoslina1()` */
  static readonly UpdateRoslina1Path = '/uzytkownikRosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslina1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslina1$Response(params: UpdateRoslina1$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return updateRoslina1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslina1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslina1(params: UpdateRoslina1$Params, context?: HttpContext): Observable<string> {
    return this.updateRoslina1$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `updateRoslinaObraz1()` */
  static readonly UpdateRoslinaObraz1Path = '/uzytkownikRosliny/{roslinaId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaObraz1()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObraz1$Response(params: UpdateRoslinaObraz1$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return updateRoslinaObraz1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaObraz1$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObraz1(params: UpdateRoslinaObraz1$Params, context?: HttpContext): Observable<{
}> {
    return this.updateRoslinaObraz1$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `findAllRoslinyOfUzytkownik()` */
  static readonly FindAllRoslinyOfUzytkownikPath = '/uzytkownikRosliny/szukaj';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllRoslinyOfUzytkownik()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  findAllRoslinyOfUzytkownik$Response(params: FindAllRoslinyOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRoslinaResponse>> {
    return findAllRoslinyOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllRoslinyOfUzytkownik$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  findAllRoslinyOfUzytkownik(params: FindAllRoslinyOfUzytkownik$Params, context?: HttpContext): Observable<PageResponseRoslinaResponse> {
    return this.findAllRoslinyOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseRoslinaResponse>): PageResponseRoslinaResponse => r.body)
    );
  }

}
