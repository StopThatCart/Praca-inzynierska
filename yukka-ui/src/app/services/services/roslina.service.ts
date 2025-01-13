/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { CechaKatalogResponse } from '../models/cecha-katalog-response';
import { CechaResponse } from '../models/cecha-response';
import { deleteRoslina } from '../fn/roslina/delete-roslina';
import { DeleteRoslina$Params } from '../fn/roslina/delete-roslina';
import { findAllRoslinyWithParameters } from '../fn/roslina/find-all-rosliny-with-parameters';
import { FindAllRoslinyWithParameters$Params } from '../fn/roslina/find-all-rosliny-with-parameters';
import { findByUuid } from '../fn/roslina/find-by-uuid';
import { FindByUuid$Params } from '../fn/roslina/find-by-uuid';
import { getCechyCountFromQuery } from '../fn/roslina/get-cechy-count-from-query';
import { GetCechyCountFromQuery$Params } from '../fn/roslina/get-cechy-count-from-query';
import { getCechyWithRelations } from '../fn/roslina/get-cechy-with-relations';
import { GetCechyWithRelations$Params } from '../fn/roslina/get-cechy-with-relations';
import { PageResponseRoslinaResponse } from '../models/page-response-roslina-response';
import { RoslinaResponse } from '../models/roslina-response';
import { saveRoslina } from '../fn/roslina/save-roslina';
import { SaveRoslina$Params } from '../fn/roslina/save-roslina';
import { updateRoslina } from '../fn/roslina/update-roslina';
import { UpdateRoslina$Params } from '../fn/roslina/update-roslina';
import { updateRoslinaObraz } from '../fn/roslina/update-roslina-obraz';
import { UpdateRoslinaObraz$Params } from '../fn/roslina/update-roslina-obraz';

@Injectable({ providedIn: 'root' })
export class RoslinaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `findByUuid()` */
  static readonly FindByUuidPath = '/rosliny/{uuid}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByUuid()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByUuid$Response(params: FindByUuid$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return findByUuid(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByUuid$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByUuid(params: FindByUuid$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.findByUuid$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /** Path part for operation `updateRoslina()` */
  static readonly UpdateRoslinaPath = '/rosliny/{uuid}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslina()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslina$Response(params: UpdateRoslina$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return updateRoslina(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslina$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslina(params: UpdateRoslina$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.updateRoslina$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /** Path part for operation `deleteRoslina()` */
  static readonly DeleteRoslinaPath = '/rosliny/{uuid}';

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

  /** Path part for operation `updateRoslinaObraz()` */
  static readonly UpdateRoslinaObrazPath = '/rosliny/{uuid}/obraz';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaObraz()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObraz$Response(params: UpdateRoslinaObraz$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return updateRoslinaObraz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaObraz$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObraz(params: UpdateRoslinaObraz$Params, context?: HttpContext): Observable<string> {
    return this.updateRoslinaObraz$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `saveRoslina()` */
  static readonly SaveRoslinaPath = '/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina$Response(params?: SaveRoslina$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return saveRoslina(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina(params?: SaveRoslina$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.saveRoslina$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /** Path part for operation `findAllRoslinyWithParameters()` */
  static readonly FindAllRoslinyWithParametersPath = '/rosliny/szukaj';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllRoslinyWithParameters()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  findAllRoslinyWithParameters$Response(params?: FindAllRoslinyWithParameters$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRoslinaResponse>> {
    return findAllRoslinyWithParameters(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllRoslinyWithParameters$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  findAllRoslinyWithParameters(params?: FindAllRoslinyWithParameters$Params, context?: HttpContext): Observable<PageResponseRoslinaResponse> {
    return this.findAllRoslinyWithParameters$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseRoslinaResponse>): PageResponseRoslinaResponse => r.body)
    );
  }

  /** Path part for operation `getCechyCountFromQuery()` */
  static readonly GetCechyCountFromQueryPath = '/rosliny/cechyQuery';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getCechyCountFromQuery()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  getCechyCountFromQuery$Response(params?: GetCechyCountFromQuery$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<CechaKatalogResponse>>> {
    return getCechyCountFromQuery(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getCechyCountFromQuery$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  getCechyCountFromQuery(params?: GetCechyCountFromQuery$Params, context?: HttpContext): Observable<Array<CechaKatalogResponse>> {
    return this.getCechyCountFromQuery$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<CechaKatalogResponse>>): Array<CechaKatalogResponse> => r.body)
    );
  }

  /** Path part for operation `getCechyWithRelations()` */
  static readonly GetCechyWithRelationsPath = '/rosliny/cechy';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getCechyWithRelations()` instead.
   *
   * This method doesn't expect any request body.
   */
  getCechyWithRelations$Response(params?: GetCechyWithRelations$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<CechaResponse>>> {
    return getCechyWithRelations(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getCechyWithRelations$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getCechyWithRelations(params?: GetCechyWithRelations$Params, context?: HttpContext): Observable<Array<CechaResponse>> {
    return this.getCechyWithRelations$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<CechaResponse>>): Array<CechaResponse> => r.body)
    );
  }

}
