/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { getAllSoils } from '../fn/soil-resource/get-all-soils';
import { GetAllSoils$Params } from '../fn/soil-resource/get-all-soils';
import { Gleba } from '../models/gleba';

@Injectable({ providedIn: 'root' })
export class SoilResourceService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getAllSoils()` */
  static readonly GetAllSoilsPath = '/rest/neo4j/gleba';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllSoils()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllSoils$Response(params?: GetAllSoils$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Gleba>>> {
    return getAllSoils(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllSoils$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllSoils(params?: GetAllSoils$Params, context?: HttpContext): Observable<Array<Gleba>> {
    return this.getAllSoils$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<Gleba>>): Array<Gleba> => r.body)
    );
  }

}
