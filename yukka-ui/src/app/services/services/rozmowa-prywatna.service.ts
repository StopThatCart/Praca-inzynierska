/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { acceptRozmowaPrywatna } from '../fn/rozmowa-prywatna/accept-rozmowa-prywatna';
import { AcceptRozmowaPrywatna$Params } from '../fn/rozmowa-prywatna/accept-rozmowa-prywatna';
import { findRozmowyPrywatneOfUzytkownik } from '../fn/rozmowa-prywatna/find-rozmowy-prywatne-of-uzytkownik';
import { FindRozmowyPrywatneOfUzytkownik$Params } from '../fn/rozmowa-prywatna/find-rozmowy-prywatne-of-uzytkownik';
import { getRozmowaPrywatna } from '../fn/rozmowa-prywatna/get-rozmowa-prywatna';
import { GetRozmowaPrywatna$Params } from '../fn/rozmowa-prywatna/get-rozmowa-prywatna';
import { getRozmowaPrywatnaById } from '../fn/rozmowa-prywatna/get-rozmowa-prywatna-by-id';
import { GetRozmowaPrywatnaById$Params } from '../fn/rozmowa-prywatna/get-rozmowa-prywatna-by-id';
import { inviteToRozmowaPrywatna } from '../fn/rozmowa-prywatna/invite-to-rozmowa-prywatna';
import { InviteToRozmowaPrywatna$Params } from '../fn/rozmowa-prywatna/invite-to-rozmowa-prywatna';
import { PageResponseRozmowaPrywatnaResponse } from '../models/page-response-rozmowa-prywatna-response';
import { rejectRozmowaPrywatna } from '../fn/rozmowa-prywatna/reject-rozmowa-prywatna';
import { RejectRozmowaPrywatna$Params } from '../fn/rozmowa-prywatna/reject-rozmowa-prywatna';
import { RozmowaPrywatna } from '../models/rozmowa-prywatna';
import { RozmowaPrywatnaResponse } from '../models/rozmowa-prywatna-response';

@Injectable({ providedIn: 'root' })
export class RozmowaPrywatnaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `rejectRozmowaPrywatna()` */
  static readonly RejectRozmowaPrywatnaPath = '/rozmowy/{nazwa}/reject';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `rejectRozmowaPrywatna()` instead.
   *
   * This method doesn't expect any request body.
   */
  rejectRozmowaPrywatna$Response(params: RejectRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatna>> {
    return rejectRozmowaPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `rejectRozmowaPrywatna$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  rejectRozmowaPrywatna(params: RejectRozmowaPrywatna$Params, context?: HttpContext): Observable<RozmowaPrywatna> {
    return this.rejectRozmowaPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<RozmowaPrywatna>): RozmowaPrywatna => r.body)
    );
  }

  /** Path part for operation `acceptRozmowaPrywatna()` */
  static readonly AcceptRozmowaPrywatnaPath = '/rozmowy/{nazwa}/accept';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `acceptRozmowaPrywatna()` instead.
   *
   * This method doesn't expect any request body.
   */
  acceptRozmowaPrywatna$Response(params: AcceptRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatna>> {
    return acceptRozmowaPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `acceptRozmowaPrywatna$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  acceptRozmowaPrywatna(params: AcceptRozmowaPrywatna$Params, context?: HttpContext): Observable<RozmowaPrywatna> {
    return this.acceptRozmowaPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<RozmowaPrywatna>): RozmowaPrywatna => r.body)
    );
  }

  /** Path part for operation `getRozmowaPrywatna()` */
  static readonly GetRozmowaPrywatnaPath = '/rozmowy/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getRozmowaPrywatna()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRozmowaPrywatna$Response(params: GetRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatnaResponse>> {
    return getRozmowaPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getRozmowaPrywatna$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRozmowaPrywatna(params: GetRozmowaPrywatna$Params, context?: HttpContext): Observable<RozmowaPrywatnaResponse> {
    return this.getRozmowaPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<RozmowaPrywatnaResponse>): RozmowaPrywatnaResponse => r.body)
    );
  }

  /** Path part for operation `inviteToRozmowaPrywatna()` */
  static readonly InviteToRozmowaPrywatnaPath = '/rozmowy/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `inviteToRozmowaPrywatna()` instead.
   *
   * This method doesn't expect any request body.
   */
  inviteToRozmowaPrywatna$Response(params: InviteToRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatna>> {
    return inviteToRozmowaPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `inviteToRozmowaPrywatna$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  inviteToRozmowaPrywatna(params: InviteToRozmowaPrywatna$Params, context?: HttpContext): Observable<RozmowaPrywatna> {
    return this.inviteToRozmowaPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<RozmowaPrywatna>): RozmowaPrywatna => r.body)
    );
  }

  /** Path part for operation `findRozmowyPrywatneOfUzytkownik()` */
  static readonly FindRozmowyPrywatneOfUzytkownikPath = '/rozmowy';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findRozmowyPrywatneOfUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  findRozmowyPrywatneOfUzytkownik$Response(params?: FindRozmowyPrywatneOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRozmowaPrywatnaResponse>> {
    return findRozmowyPrywatneOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findRozmowyPrywatneOfUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findRozmowyPrywatneOfUzytkownik(params?: FindRozmowyPrywatneOfUzytkownik$Params, context?: HttpContext): Observable<PageResponseRozmowaPrywatnaResponse> {
    return this.findRozmowyPrywatneOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseRozmowaPrywatnaResponse>): PageResponseRozmowaPrywatnaResponse => r.body)
    );
  }

  /** Path part for operation `getRozmowaPrywatnaById()` */
  static readonly GetRozmowaPrywatnaByIdPath = '/rozmowy/id/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getRozmowaPrywatnaById()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRozmowaPrywatnaById$Response(params: GetRozmowaPrywatnaById$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatnaResponse>> {
    return getRozmowaPrywatnaById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getRozmowaPrywatnaById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRozmowaPrywatnaById(params: GetRozmowaPrywatnaById$Params, context?: HttpContext): Observable<RozmowaPrywatnaResponse> {
    return this.getRozmowaPrywatnaById$Response(params, context).pipe(
      map((r: StrictHttpResponse<RozmowaPrywatnaResponse>): RozmowaPrywatnaResponse => r.body)
    );
  }

}
