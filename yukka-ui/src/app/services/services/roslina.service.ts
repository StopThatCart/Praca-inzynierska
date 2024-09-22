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
import { findAllRoslinyWithParameters } from '../fn/roslina/find-all-rosliny-with-parameters';
import { FindAllRoslinyWithParameters$Params } from '../fn/roslina/find-all-rosliny-with-parameters';
import { findByNazwaLacinska } from '../fn/roslina/find-by-nazwa-lacinska';
import { FindByNazwaLacinska$Params } from '../fn/roslina/find-by-nazwa-lacinska';
import { getWlasciwosciWithRelations } from '../fn/roslina/get-wlasciwosci-with-relations';
import { GetWlasciwosciWithRelations$Params } from '../fn/roslina/get-wlasciwosci-with-relations';
import { PageResponseRoslinaResponse } from '../models/page-response-roslina-response';
import { RoslinaResponse } from '../models/roslina-response';
import { saveRoslina2$FormData } from '../fn/roslina/save-roslina-2-form-data';
import { SaveRoslina2$FormData$Params } from '../fn/roslina/save-roslina-2-form-data';
import { saveRoslina2$Json } from '../fn/roslina/save-roslina-2-json';
import { SaveRoslina2$Json$Params } from '../fn/roslina/save-roslina-2-json';
import { updateRoslina } from '../fn/roslina/update-roslina';
import { UpdateRoslina$Params } from '../fn/roslina/update-roslina';
import { updateRoslinaObraz } from '../fn/roslina/update-roslina-obraz';
import { UpdateRoslinaObraz$Params } from '../fn/roslina/update-roslina-obraz';
import { WlasciwoscResponse } from '../models/wlasciwosc-response';

@Injectable({ providedIn: 'root' })
export class RoslinaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `findByNazwaLacinska()` */
  static readonly FindByNazwaLacinskaPath = '/rosliny/{nazwa-lacinska}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByNazwaLacinska()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByNazwaLacinska$Response(params: FindByNazwaLacinska$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return findByNazwaLacinska(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByNazwaLacinska$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByNazwaLacinska(params: FindByNazwaLacinska$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.findByNazwaLacinska$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /** Path part for operation `updateRoslina()` */
  static readonly UpdateRoslinaPath = '/rosliny/{nazwa-lacinska}';

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

  /** Path part for operation `deleteRoslina1()` */
  static readonly DeleteRoslina1Path = '/rosliny/{nazwa-lacinska}';

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

  /** Path part for operation `updateRoslinaObraz()` */
  static readonly UpdateRoslinaObrazPath = '/rosliny/{nazwa-lacinska}/obraz';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaObraz()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObraz$Response(params: UpdateRoslinaObraz$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return updateRoslinaObraz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaObraz$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObraz(params: UpdateRoslinaObraz$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.updateRoslinaObraz$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /** Path part for operation `saveRoslina2()` */
  static readonly SaveRoslina2Path = '/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina2$FormData()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina2$FormData$Response(params: SaveRoslina2$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return saveRoslina2$FormData(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina2$FormData$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslina2$FormData(params: SaveRoslina2$FormData$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.saveRoslina2$FormData$Response(params, context).pipe(
      map((r: StrictHttpResponse<RoslinaResponse>): RoslinaResponse => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslina2$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina2$Json$Response(params: SaveRoslina2$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
    return saveRoslina2$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslina2$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslina2$Json(params: SaveRoslina2$Json$Params, context?: HttpContext): Observable<RoslinaResponse> {
    return this.saveRoslina2$Json$Response(params, context).pipe(
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
  findAllRoslinyWithParameters$Response(params: FindAllRoslinyWithParameters$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRoslinaResponse>> {
    return findAllRoslinyWithParameters(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllRoslinyWithParameters$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  findAllRoslinyWithParameters(params: FindAllRoslinyWithParameters$Params, context?: HttpContext): Observable<PageResponseRoslinaResponse> {
    return this.findAllRoslinyWithParameters$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseRoslinaResponse>): PageResponseRoslinaResponse => r.body)
    );
  }

  /** Path part for operation `getWlasciwosciWithRelations()` */
  static readonly GetWlasciwosciWithRelationsPath = '/rosliny/wlasciwosci';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getWlasciwosciWithRelations()` instead.
   *
   * This method doesn't expect any request body.
   */
  getWlasciwosciWithRelations$Response(params?: GetWlasciwosciWithRelations$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<WlasciwoscResponse>>> {
    return getWlasciwosciWithRelations(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getWlasciwosciWithRelations$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getWlasciwosciWithRelations(params?: GetWlasciwosciWithRelations$Params, context?: HttpContext): Observable<Array<WlasciwoscResponse>> {
    return this.getWlasciwosciWithRelations$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<WlasciwoscResponse>>): Array<WlasciwoscResponse> => r.body)
    );
  }

}
