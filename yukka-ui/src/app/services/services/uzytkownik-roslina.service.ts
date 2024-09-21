/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { deleteRoslina } from '../fn/uzytkownik-roslina/delete-roslina';
import { DeleteRoslina$Params } from '../fn/uzytkownik-roslina/delete-roslina';
import { findAllRosliny } from '../fn/uzytkownik-roslina/find-all-rosliny';
import { FindAllRosliny$Params } from '../fn/uzytkownik-roslina/find-all-rosliny';
import { findUzytkownikRoslinaByRoslinaId } from '../fn/uzytkownik-roslina/find-uzytkownik-roslina-by-roslina-id';
import { FindUzytkownikRoslinaByRoslinaId$Params } from '../fn/uzytkownik-roslina/find-uzytkownik-roslina-by-roslina-id';
import { PageResponseUzytkownikRoslinaResponse } from '../models/page-response-uzytkownik-roslina-response';
import { saveRoslina1$FormData } from '../fn/uzytkownik-roslina/save-roslina-1-form-data';
import { SaveRoslina1$FormData$Params } from '../fn/uzytkownik-roslina/save-roslina-1-form-data';
import { saveRoslina1$Json } from '../fn/uzytkownik-roslina/save-roslina-1-json';
import { SaveRoslina1$Json$Params } from '../fn/uzytkownik-roslina/save-roslina-1-json';
import { updateRoslina1 } from '../fn/uzytkownik-roslina/update-roslina-1';
import { UpdateRoslina1$Params } from '../fn/uzytkownik-roslina/update-roslina-1';
import { updateRoslinaObraz1 } from '../fn/uzytkownik-roslina/update-roslina-obraz-1';
import { UpdateRoslinaObraz1$Params } from '../fn/uzytkownik-roslina/update-roslina-obraz-1';
import { UzytkownikRoslina } from '../models/uzytkownik-roslina';

@Injectable({ providedIn: 'root' })
export class UzytkownikRoslinaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `findAllRosliny()` */
  static readonly FindAllRoslinyPath = '/uzytkownikRosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllRosliny()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllRosliny$Response(params?: FindAllRosliny$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseUzytkownikRoslinaResponse>> {
    return findAllRosliny(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllRosliny$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllRosliny(params?: FindAllRosliny$Params, context?: HttpContext): Observable<PageResponseUzytkownikRoslinaResponse> {
    return this.findAllRosliny$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseUzytkownikRoslinaResponse>): PageResponseUzytkownikRoslinaResponse => r.body)
    );
  }

  /** Path part for operation `saveRoslina1()` */
  static readonly SaveRoslina1Path = '/uzytkownikRosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina1$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina1$Json$Response(params: SaveRoslina1$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return saveRoslina1$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina1$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina1$Json(params: SaveRoslina1$Json$Params, context?: HttpContext): Observable<string> {
    return this.saveRoslina1$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina1$FormData()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina1$FormData$Response(params: SaveRoslina1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return saveRoslina1$FormData(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina1$FormData$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina1$FormData(params: SaveRoslina1$FormData$Params, context?: HttpContext): Observable<string> {
    return this.saveRoslina1$FormData$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
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

  /** Path part for operation `findUzytkownikRoslinaByRoslinaId()` */
  static readonly FindUzytkownikRoslinaByRoslinaIdPath = '/uzytkownikRosliny/{roslinaId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findUzytkownikRoslinaByRoslinaId()` instead.
   *
   * This method doesn't expect any request body.
   */
  findUzytkownikRoslinaByRoslinaId$Response(params: FindUzytkownikRoslinaByRoslinaId$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikRoslina>> {
    return findUzytkownikRoslinaByRoslinaId(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findUzytkownikRoslinaByRoslinaId$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findUzytkownikRoslinaByRoslinaId(params: FindUzytkownikRoslinaByRoslinaId$Params, context?: HttpContext): Observable<UzytkownikRoslina> {
    return this.findUzytkownikRoslinaByRoslinaId$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikRoslina>): UzytkownikRoslina => r.body)
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

  /** Path part for operation `deleteRoslina()` */
  static readonly DeleteRoslinaPath = '/uzytkownikRosliny/{roslinaId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteRoslina()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteRoslina$Response(params: DeleteRoslina$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return deleteRoslina(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteRoslina$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteRoslina(params: DeleteRoslina$Params, context?: HttpContext): Observable<string> {
    return this.deleteRoslina$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
