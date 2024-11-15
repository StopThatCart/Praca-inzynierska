/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { findAllOgrody } from '../fn/ogrod/find-all-ogrody';
import { FindAllOgrody$Params } from '../fn/ogrod/find-all-ogrody';
import { getDzialki } from '../fn/ogrod/get-dzialki';
import { GetDzialki$Params } from '../fn/ogrod/get-dzialki';
import { OgrodResponse } from '../models/ogrod-response';
import { PageResponseOgrodResponse } from '../models/page-response-ogrod-response';
import { setOgrodNazwa } from '../fn/ogrod/set-ogrod-nazwa';
import { SetOgrodNazwa$Params } from '../fn/ogrod/set-ogrod-nazwa';

@Injectable({ providedIn: 'root' })
export class OgrodService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `setOgrodNazwa()` */
  static readonly SetOgrodNazwaPath = '/ogrody/{ogrod-nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `setOgrodNazwa()` instead.
   *
   * This method doesn't expect any request body.
   */
  setOgrodNazwa$Response(params: SetOgrodNazwa$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return setOgrodNazwa(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `setOgrodNazwa$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  setOgrodNazwa(params: SetOgrodNazwa$Params, context?: HttpContext): Observable<string> {
    return this.setOgrodNazwa$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `findAllOgrody()` */
  static readonly FindAllOgrodyPath = '/ogrody';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllOgrody()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllOgrody$Response(params?: FindAllOgrody$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseOgrodResponse>> {
    return findAllOgrody(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllOgrody$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllOgrody(params?: FindAllOgrody$Params, context?: HttpContext): Observable<PageResponseOgrodResponse> {
    return this.findAllOgrody$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseOgrodResponse>): PageResponseOgrodResponse => r.body)
    );
  }

  /** Path part for operation `getDzialki()` */
  static readonly GetDzialkiPath = '/ogrody/{uzytkownik-nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getDzialki()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialki$Response(params: GetDzialki$Params, context?: HttpContext): Observable<StrictHttpResponse<OgrodResponse>> {
    return getDzialki(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getDzialki$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialki(params: GetDzialki$Params, context?: HttpContext): Observable<OgrodResponse> {
    return this.getDzialki$Response(params, context).pipe(
      map((r: StrictHttpResponse<OgrodResponse>): OgrodResponse => r.body)
    );
  }

}
