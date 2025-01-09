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
import { addKomentarzToWiadomoscPrywatna } from '../fn/rozmowa-prywatna/add-komentarz-to-wiadomosc-prywatna';
import { AddKomentarzToWiadomoscPrywatna$Params } from '../fn/rozmowa-prywatna/add-komentarz-to-wiadomosc-prywatna';
import { findRozmowaPrywatnaByNazwa } from '../fn/rozmowa-prywatna/find-rozmowa-prywatna-by-nazwa';
import { FindRozmowaPrywatnaByNazwa$Params } from '../fn/rozmowa-prywatna/find-rozmowa-prywatna-by-nazwa';
import { findRozmowyPrywatneOfUzytkownik } from '../fn/rozmowa-prywatna/find-rozmowy-prywatne-of-uzytkownik';
import { FindRozmowyPrywatneOfUzytkownik$Params } from '../fn/rozmowa-prywatna/find-rozmowy-prywatne-of-uzytkownik';
import { inviteToRozmowaPrywatna } from '../fn/rozmowa-prywatna/invite-to-rozmowa-prywatna';
import { InviteToRozmowaPrywatna$Params } from '../fn/rozmowa-prywatna/invite-to-rozmowa-prywatna';
import { KomentarzResponse } from '../models/komentarz-response';
import { PageResponseRozmowaPrywatnaResponse } from '../models/page-response-rozmowa-prywatna-response';
import { rejectRozmowaPrywatna } from '../fn/rozmowa-prywatna/reject-rozmowa-prywatna';
import { RejectRozmowaPrywatna$Params } from '../fn/rozmowa-prywatna/reject-rozmowa-prywatna';
import { RozmowaPrywatnaResponse } from '../models/rozmowa-prywatna-response';

@Injectable({ providedIn: 'root' })
export class RozmowaPrywatnaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `rejectRozmowaPrywatna()` */
  static readonly RejectRozmowaPrywatnaPath = '/rozmowy/{uzytkownik-nazwa}/reject';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `rejectRozmowaPrywatna()` instead.
   *
   * This method doesn't expect any request body.
   */
  rejectRozmowaPrywatna$Response(params: RejectRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return rejectRozmowaPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `rejectRozmowaPrywatna$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  rejectRozmowaPrywatna(params: RejectRozmowaPrywatna$Params, context?: HttpContext): Observable<{
}> {
    return this.rejectRozmowaPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `acceptRozmowaPrywatna()` */
  static readonly AcceptRozmowaPrywatnaPath = '/rozmowy/{uzytkownik-nazwa}/accept';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `acceptRozmowaPrywatna()` instead.
   *
   * This method doesn't expect any request body.
   */
  acceptRozmowaPrywatna$Response(params: AcceptRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return acceptRozmowaPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `acceptRozmowaPrywatna$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  acceptRozmowaPrywatna(params: AcceptRozmowaPrywatna$Params, context?: HttpContext): Observable<{
}> {
    return this.acceptRozmowaPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `findRozmowaPrywatnaByNazwa()` */
  static readonly FindRozmowaPrywatnaByNazwaPath = '/rozmowy/{uzytkownik-nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findRozmowaPrywatnaByNazwa()` instead.
   *
   * This method doesn't expect any request body.
   */
  findRozmowaPrywatnaByNazwa$Response(params: FindRozmowaPrywatnaByNazwa$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatnaResponse>> {
    return findRozmowaPrywatnaByNazwa(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findRozmowaPrywatnaByNazwa$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findRozmowaPrywatnaByNazwa(params: FindRozmowaPrywatnaByNazwa$Params, context?: HttpContext): Observable<RozmowaPrywatnaResponse> {
    return this.findRozmowaPrywatnaByNazwa$Response(params, context).pipe(
      map((r: StrictHttpResponse<RozmowaPrywatnaResponse>): RozmowaPrywatnaResponse => r.body)
    );
  }

  /** Path part for operation `inviteToRozmowaPrywatna()` */
  static readonly InviteToRozmowaPrywatnaPath = '/rozmowy/{uzytkownik-nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `inviteToRozmowaPrywatna()` instead.
   *
   * This method doesn't expect any request body.
   */
  inviteToRozmowaPrywatna$Response(params: InviteToRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return inviteToRozmowaPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `inviteToRozmowaPrywatna$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  inviteToRozmowaPrywatna(params: InviteToRozmowaPrywatna$Params, context?: HttpContext): Observable<{
}> {
    return this.inviteToRozmowaPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `addKomentarzToWiadomoscPrywatna()` */
  static readonly AddKomentarzToWiadomoscPrywatnaPath = '/rozmowy/wiadomosc';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addKomentarzToWiadomoscPrywatna()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToWiadomoscPrywatna$Response(params?: AddKomentarzToWiadomoscPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
    return addKomentarzToWiadomoscPrywatna(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addKomentarzToWiadomoscPrywatna$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToWiadomoscPrywatna(params?: AddKomentarzToWiadomoscPrywatna$Params, context?: HttpContext): Observable<KomentarzResponse> {
    return this.addKomentarzToWiadomoscPrywatna$Response(params, context).pipe(
      map((r: StrictHttpResponse<KomentarzResponse>): KomentarzResponse => r.body)
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

}
