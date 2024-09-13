/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { getNieprzeczytaneCountOfUzytkownik } from '../fn/powiadomienie/get-nieprzeczytane-count-of-uzytkownik';
import { GetNieprzeczytaneCountOfUzytkownik$Params } from '../fn/powiadomienie/get-nieprzeczytane-count-of-uzytkownik';
import { getPowiadomienia } from '../fn/powiadomienie/get-powiadomienia';
import { GetPowiadomienia$Params } from '../fn/powiadomienie/get-powiadomienia';
import { PageResponsePowiadomienieResponse } from '../models/page-response-powiadomienie-response';
import { PowiadomienieResponse } from '../models/powiadomienie-response';
import { remove1 } from '../fn/powiadomienie/remove-1';
import { Remove1$Params } from '../fn/powiadomienie/remove-1';
import { sendSpecjalnePowiadomienie } from '../fn/powiadomienie/send-specjalne-powiadomienie';
import { SendSpecjalnePowiadomienie$Params } from '../fn/powiadomienie/send-specjalne-powiadomienie';
import { sendSpecjalnePowiadomienieToPracownicy } from '../fn/powiadomienie/send-specjalne-powiadomienie-to-pracownicy';
import { SendSpecjalnePowiadomienieToPracownicy$Params } from '../fn/powiadomienie/send-specjalne-powiadomienie-to-pracownicy';
import { setPowiadomieniePrzeczytane } from '../fn/powiadomienie/set-powiadomienie-przeczytane';
import { SetPowiadomieniePrzeczytane$Params } from '../fn/powiadomienie/set-powiadomienie-przeczytane';

@Injectable({ providedIn: 'root' })
export class PowiadomienieService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `sendSpecjalnePowiadomienie()` */
  static readonly SendSpecjalnePowiadomieniePath = '/powiadomienia/pracownik';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `sendSpecjalnePowiadomienie()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienie$Response(params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<StrictHttpResponse<PowiadomienieResponse>> {
    return sendSpecjalnePowiadomienie(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendSpecjalnePowiadomienie$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienie(params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<PowiadomienieResponse> {
    return this.sendSpecjalnePowiadomienie$Response(params, context).pipe(
      map((r: StrictHttpResponse<PowiadomienieResponse>): PowiadomienieResponse => r.body)
    );
  }

  /** Path part for operation `sendSpecjalnePowiadomienieToPracownicy()` */
  static readonly SendSpecjalnePowiadomienieToPracownicyPath = '/powiadomienia/admin';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `sendSpecjalnePowiadomienieToPracownicy()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienieToPracownicy$Response(params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<PowiadomienieResponse>> {
    return sendSpecjalnePowiadomienieToPracownicy(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendSpecjalnePowiadomienieToPracownicy$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienieToPracownicy(params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<PowiadomienieResponse> {
    return this.sendSpecjalnePowiadomienieToPracownicy$Response(params, context).pipe(
      map((r: StrictHttpResponse<PowiadomienieResponse>): PowiadomienieResponse => r.body)
    );
  }

  /** Path part for operation `setPowiadomieniePrzeczytane()` */
  static readonly SetPowiadomieniePrzeczytanePath = '/powiadomienia/{id}/przeczytane';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `setPowiadomieniePrzeczytane()` instead.
   *
   * This method doesn't expect any request body.
   */
  setPowiadomieniePrzeczytane$Response(params: SetPowiadomieniePrzeczytane$Params, context?: HttpContext): Observable<StrictHttpResponse<PowiadomienieResponse>> {
    return setPowiadomieniePrzeczytane(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `setPowiadomieniePrzeczytane$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  setPowiadomieniePrzeczytane(params: SetPowiadomieniePrzeczytane$Params, context?: HttpContext): Observable<PowiadomienieResponse> {
    return this.setPowiadomieniePrzeczytane$Response(params, context).pipe(
      map((r: StrictHttpResponse<PowiadomienieResponse>): PowiadomienieResponse => r.body)
    );
  }

  /** Path part for operation `getPowiadomienia()` */
  static readonly GetPowiadomieniaPath = '/powiadomienia';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getPowiadomienia()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPowiadomienia$Response(params?: GetPowiadomienia$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePowiadomienieResponse>> {
    return getPowiadomienia(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getPowiadomienia$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPowiadomienia(params?: GetPowiadomienia$Params, context?: HttpContext): Observable<PageResponsePowiadomienieResponse> {
    return this.getPowiadomienia$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponsePowiadomienieResponse>): PageResponsePowiadomienieResponse => r.body)
    );
  }

  /** Path part for operation `getNieprzeczytaneCountOfUzytkownik()` */
  static readonly GetNieprzeczytaneCountOfUzytkownikPath = '/powiadomienia/nieprzeczytane/count';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getNieprzeczytaneCountOfUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  getNieprzeczytaneCountOfUzytkownik$Response(params?: GetNieprzeczytaneCountOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return getNieprzeczytaneCountOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getNieprzeczytaneCountOfUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getNieprzeczytaneCountOfUzytkownik(params?: GetNieprzeczytaneCountOfUzytkownik$Params, context?: HttpContext): Observable<number> {
    return this.getNieprzeczytaneCountOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `remove1()` */
  static readonly Remove1Path = '/powiadomienia/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `remove1()` instead.
   *
   * This method doesn't expect any request body.
   */
  remove1$Response(params: Remove1$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return remove1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `remove1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  remove1(params: Remove1$Params, context?: HttpContext): Observable<{
}> {
    return this.remove1$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

}
