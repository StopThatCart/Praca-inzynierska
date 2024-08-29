/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { deleteRoslina1 } from '../fn/roslina/delete-roslina-1';
import { DeleteRoslina1$Params } from '../fn/roslina/delete-roslina-1';
import { findAllRosliny1 } from '../fn/roslina/find-all-rosliny-1';
import { FindAllRosliny1$Params } from '../fn/roslina/find-all-rosliny-1';
import { getByNazwaLacinska } from '../fn/roslina/get-by-nazwa-lacinska';
import { GetByNazwaLacinska$Params } from '../fn/roslina/get-by-nazwa-lacinska';
import { PageResponseRoslinaResponse } from '../models/page-response-roslina-response';
import { Roslina } from '../models/roslina';
import { saveRoslina2$FormData$Any } from '../fn/roslina/save-roslina-2-form-data-any';
import { SaveRoslina2$FormData$Any$Params } from '../fn/roslina/save-roslina-2-form-data-any';
import { saveRoslina2$FormData$Json } from '../fn/roslina/save-roslina-2-form-data-json';
import { SaveRoslina2$FormData$Json$Params } from '../fn/roslina/save-roslina-2-form-data-json';
import { saveRoslina2$Json$Any } from '../fn/roslina/save-roslina-2-json-any';
import { SaveRoslina2$Json$Any$Params } from '../fn/roslina/save-roslina-2-json-any';
import { saveRoslina2$Json$Json } from '../fn/roslina/save-roslina-2-json-json';
import { SaveRoslina2$Json$Json$Params } from '../fn/roslina/save-roslina-2-json-json';
import { updateRoslina } from '../fn/roslina/update-roslina';
import { UpdateRoslina$Params } from '../fn/roslina/update-roslina';

@Injectable({ providedIn: 'root' })
export class RoslinaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `findAllRosliny1()` */
  static readonly FindAllRosliny1Path = '/rest/neo4j/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllRosliny1()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllRosliny1$Response(params?: FindAllRosliny1$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRoslinaResponse>> {
    return findAllRosliny1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllRosliny1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllRosliny1(params?: FindAllRosliny1$Params, context?: HttpContext): Observable<PageResponseRoslinaResponse> {
    return this.findAllRosliny1$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseRoslinaResponse>): PageResponseRoslinaResponse => r.body)
    );
  }

  /** Path part for operation `updateRoslina()` */
  static readonly UpdateRoslinaPath = '/rest/neo4j/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslina()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslina$Response(params: UpdateRoslina$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return updateRoslina(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslina$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslina(params: UpdateRoslina$Params, context?: HttpContext): Observable<string> {
    return this.updateRoslina$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `saveRoslina2()` */
  static readonly SaveRoslina2Path = '/rest/neo4j/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina2$Json$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina2$Json$Json$Response(params: SaveRoslina2$Json$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return saveRoslina2$Json$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina2$Json$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina2$Json$Json(params: SaveRoslina2$Json$Json$Params, context?: HttpContext): Observable<string> {
    return this.saveRoslina2$Json$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina2$Json$Any()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina2$Json$Any$Response(params: SaveRoslina2$Json$Any$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return saveRoslina2$Json$Any(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina2$Json$Any$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina2$Json$Any(params: SaveRoslina2$Json$Any$Params, context?: HttpContext): Observable<string> {
    return this.saveRoslina2$Json$Any$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina2$FormData$Json()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina2$FormData$Json$Response(params: SaveRoslina2$FormData$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return saveRoslina2$FormData$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina2$FormData$Json$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina2$FormData$Json(params: SaveRoslina2$FormData$Json$Params, context?: HttpContext): Observable<string> {
    return this.saveRoslina2$FormData$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina2$FormData$Any()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina2$FormData$Any$Response(params: SaveRoslina2$FormData$Any$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return saveRoslina2$FormData$Any(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina2$FormData$Any$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina2$FormData$Any(params: SaveRoslina2$FormData$Any$Params, context?: HttpContext): Observable<string> {
    return this.saveRoslina2$FormData$Any$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `getByNazwaLacinska()` */
  static readonly GetByNazwaLacinskaPath = '/rest/neo4j/rosliny/{nazwa-lacinska}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getByNazwaLacinska()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByNazwaLacinska$Response(params: GetByNazwaLacinska$Params, context?: HttpContext): Observable<StrictHttpResponse<Roslina>> {
    return getByNazwaLacinska(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getByNazwaLacinska$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByNazwaLacinska(params: GetByNazwaLacinska$Params, context?: HttpContext): Observable<Roslina> {
    return this.getByNazwaLacinska$Response(params, context).pipe(
      map((r: StrictHttpResponse<Roslina>): Roslina => r.body)
    );
  }

  /** Path part for operation `deleteRoslina1()` */
  static readonly DeleteRoslina1Path = '/rest/neo4j/rosliny/{nazwaLacinska}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteRoslina1()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteRoslina1$Response(params: DeleteRoslina1$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return deleteRoslina1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteRoslina1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteRoslina1(params: DeleteRoslina1$Params, context?: HttpContext): Observable<string> {
    return this.deleteRoslina1$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
