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
import { getUzytkownikWlasciwosciCountFromQuery } from '../fn/uzytkownik-roslina/get-uzytkownik-wlasciwosci-count-from-query';
import { GetUzytkownikWlasciwosciCountFromQuery$Params } from '../fn/uzytkownik-roslina/get-uzytkownik-wlasciwosci-count-from-query';
import { PageResponseRoslinaResponse } from '../models/page-response-roslina-response';
import { RoslinaResponse } from '../models/roslina-response';
import { save } from '../fn/uzytkownik-roslina/save';
import { Save$Params } from '../fn/uzytkownik-roslina/save';
import { update } from '../fn/uzytkownik-roslina/update';
import { Update$Params } from '../fn/uzytkownik-roslina/update';
import { updateObraz } from '../fn/uzytkownik-roslina/update-obraz';
import { UpdateObraz$Params } from '../fn/uzytkownik-roslina/update-obraz';
import { WlasciwoscKatalogResponse } from '../models/wlasciwosc-katalog-response';

@Injectable({ providedIn: 'root' })
export class UzytkownikRoslinaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `updateObraz()` */
  static readonly UpdateObrazPath = '/uzytkownikRosliny/{roslinaId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateObraz()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateObraz$Response(params: UpdateObraz$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return updateObraz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateObraz$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateObraz(params: UpdateObraz$Params, context?: HttpContext): Observable<{
}> {
    return this.updateObraz$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `save()` */
  static readonly SavePath = '/uzytkownikRosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `save()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  save$Response(params?: Save$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return save(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `save$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  save(params?: Save$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.save$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /** Path part for operation `update()` */
  static readonly UpdatePath = '/uzytkownikRosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update$Response(params: Update$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return update(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update(params: Update$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.update$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /** Path part for operation `getUzytkownikWlasciwosciCountFromQuery()` */
  static readonly GetUzytkownikWlasciwosciCountFromQueryPath = '/uzytkownikRosliny/wlasciwosciQuery';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getUzytkownikWlasciwosciCountFromQuery()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  getUzytkownikWlasciwosciCountFromQuery$Response(params?: GetUzytkownikWlasciwosciCountFromQuery$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<WlasciwoscKatalogResponse>>> {
    return getUzytkownikWlasciwosciCountFromQuery(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getUzytkownikWlasciwosciCountFromQuery$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  getUzytkownikWlasciwosciCountFromQuery(params?: GetUzytkownikWlasciwosciCountFromQuery$Params, context?: HttpContext): Observable<Array<WlasciwoscKatalogResponse>> {
    return this.getUzytkownikWlasciwosciCountFromQuery$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<WlasciwoscKatalogResponse>>): Array<WlasciwoscKatalogResponse> => r.body)
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
