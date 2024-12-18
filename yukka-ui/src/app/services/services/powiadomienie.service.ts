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
import { removePowiadomienie } from '../fn/powiadomienie/remove-powiadomienie';
import { RemovePowiadomienie$Params } from '../fn/powiadomienie/remove-powiadomienie';
import { sendSpecjalnePowiadomienie } from '../fn/powiadomienie/send-specjalne-powiadomienie';
import { SendSpecjalnePowiadomienie$Params } from '../fn/powiadomienie/send-specjalne-powiadomienie';
import { sendSpecjalnePowiadomienieToPracownicy } from '../fn/powiadomienie/send-specjalne-powiadomienie-to-pracownicy';
import { SendSpecjalnePowiadomienieToPracownicy$Params } from '../fn/powiadomienie/send-specjalne-powiadomienie-to-pracownicy';
import { sendZgloszenie } from '../fn/powiadomienie/send-zgloszenie';
import { SendZgloszenie$Params } from '../fn/powiadomienie/send-zgloszenie';
import { setAllPowiadomieniaPrzeczytane } from '../fn/powiadomienie/set-all-powiadomienia-przeczytane';
import { SetAllPowiadomieniaPrzeczytane$Params } from '../fn/powiadomienie/set-all-powiadomienia-przeczytane';
import { setPowiadomieniePrzeczytane } from '../fn/powiadomienie/set-powiadomienie-przeczytane';
import { SetPowiadomieniePrzeczytane$Params } from '../fn/powiadomienie/set-powiadomienie-przeczytane';
import { ukryjPowiadomienie } from '../fn/powiadomienie/ukryj-powiadomienie';
import { UkryjPowiadomienie$Params } from '../fn/powiadomienie/ukryj-powiadomienie';

@Injectable({ providedIn: 'root' })
export class PowiadomienieService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `sendZgloszenie()` */
  static readonly SendZgloszeniePath = '/powiadomienia/zgloszenie';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `sendZgloszenie()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendZgloszenie$Response(params: SendZgloszenie$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return sendZgloszenie(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendZgloszenie$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendZgloszenie(params: SendZgloszenie$Params, context?: HttpContext): Observable<{
}> {
    return this.sendZgloszenie$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `sendSpecjalnePowiadomienie()` */
  static readonly SendSpecjalnePowiadomieniePath = '/powiadomienia/pracownik';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `sendSpecjalnePowiadomienie()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienie$Response(params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return sendSpecjalnePowiadomienie(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendSpecjalnePowiadomienie$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienie(params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<{
}> {
    return this.sendSpecjalnePowiadomienie$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
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
  sendSpecjalnePowiadomienieToPracownicy$Response(params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return sendSpecjalnePowiadomienieToPracownicy(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendSpecjalnePowiadomienieToPracownicy$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienieToPracownicy(params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<{
}> {
    return this.sendSpecjalnePowiadomienieToPracownicy$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `ukryjPowiadomienie()` */
  static readonly UkryjPowiadomieniePath = '/powiadomienia/{id}/ukryte';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `ukryjPowiadomienie()` instead.
   *
   * This method doesn't expect any request body.
   */
  ukryjPowiadomienie$Response(params: UkryjPowiadomienie$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return ukryjPowiadomienie(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `ukryjPowiadomienie$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  ukryjPowiadomienie(params: UkryjPowiadomienie$Params, context?: HttpContext): Observable<{
}> {
    return this.ukryjPowiadomienie$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
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

  /** Path part for operation `setAllPowiadomieniaPrzeczytane()` */
  static readonly SetAllPowiadomieniaPrzeczytanePath = '/powiadomienia/przeczytane';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `setAllPowiadomieniaPrzeczytane()` instead.
   *
   * This method doesn't expect any request body.
   */
  setAllPowiadomieniaPrzeczytane$Response(params?: SetAllPowiadomieniaPrzeczytane$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return setAllPowiadomieniaPrzeczytane(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `setAllPowiadomieniaPrzeczytane$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  setAllPowiadomieniaPrzeczytane(params?: SetAllPowiadomieniaPrzeczytane$Params, context?: HttpContext): Observable<{
}> {
    return this.setAllPowiadomieniaPrzeczytane$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
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

  /** Path part for operation `removePowiadomienie()` */
  static readonly RemovePowiadomieniePath = '/powiadomienia/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removePowiadomienie()` instead.
   *
   * This method doesn't expect any request body.
   */
  removePowiadomienie$Response(params: RemovePowiadomienie$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return removePowiadomienie(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removePowiadomienie$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  removePowiadomienie(params: RemovePowiadomienie$Params, context?: HttpContext): Observable<{
}> {
    return this.removePowiadomienie$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

}
