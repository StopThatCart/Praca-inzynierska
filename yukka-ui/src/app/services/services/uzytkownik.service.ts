/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { findAllUzytkownicy } from '../fn/uzytkownik/find-all-uzytkownicy';
import { FindAllUzytkownicy$Params } from '../fn/uzytkownik/find-all-uzytkownicy';
import { getByEmail } from '../fn/uzytkownik/get-by-email';
import { GetByEmail$Params } from '../fn/uzytkownik/get-by-email';
import { Powiadomienie } from '../models/powiadomienie';
import { remove } from '../fn/uzytkownik/remove';
import { Remove$Params } from '../fn/uzytkownik/remove';
import { removeSelf } from '../fn/uzytkownik/remove-self';
import { RemoveSelf$Params } from '../fn/uzytkownik/remove-self';
import { sendSpecjalnePowiadomienie } from '../fn/uzytkownik/send-specjalne-powiadomienie';
import { SendSpecjalnePowiadomienie$Params } from '../fn/uzytkownik/send-specjalne-powiadomienie';
import { sendSpecjalnePowiadomienieToPracownicy } from '../fn/uzytkownik/send-specjalne-powiadomienie-to-pracownicy';
import { SendSpecjalnePowiadomienieToPracownicy$Params } from '../fn/uzytkownik/send-specjalne-powiadomienie-to-pracownicy';
import { setBanUzytkownik } from '../fn/uzytkownik/set-ban-uzytkownik';
import { SetBanUzytkownik$Params } from '../fn/uzytkownik/set-ban-uzytkownik';
import { updateAvatar } from '../fn/uzytkownik/update-avatar';
import { UpdateAvatar$Params } from '../fn/uzytkownik/update-avatar';
import { Uzytkownik } from '../models/uzytkownik';
import { UzytkownikResponse } from '../models/uzytkownik-response';

@Injectable({ providedIn: 'root' })
export class UzytkownikService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `sendSpecjalnePowiadomienie()` */
  static readonly SendSpecjalnePowiadomieniePath = '/rest/neo4j/uzytkownicy/pracownik/powiadomienie';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `sendSpecjalnePowiadomienie()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienie$Response(params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<StrictHttpResponse<Powiadomienie>> {
    return sendSpecjalnePowiadomienie(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendSpecjalnePowiadomienie$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienie(params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<Powiadomienie> {
    return this.sendSpecjalnePowiadomienie$Response(params, context).pipe(
      map((r: StrictHttpResponse<Powiadomienie>): Powiadomienie => r.body)
    );
  }

  /** Path part for operation `sendSpecjalnePowiadomienieToPracownicy()` */
  static readonly SendSpecjalnePowiadomienieToPracownicyPath = '/rest/neo4j/uzytkownicy/admin/powiadomienie';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `sendSpecjalnePowiadomienieToPracownicy()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienieToPracownicy$Response(params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<Powiadomienie>> {
    return sendSpecjalnePowiadomienieToPracownicy(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendSpecjalnePowiadomienieToPracownicy$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendSpecjalnePowiadomienieToPracownicy(params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<Powiadomienie> {
    return this.sendSpecjalnePowiadomienieToPracownicy$Response(params, context).pipe(
      map((r: StrictHttpResponse<Powiadomienie>): Powiadomienie => r.body)
    );
  }

  /** Path part for operation `setBanUzytkownik()` */
  static readonly SetBanUzytkownikPath = '/rest/neo4j/uzytkownicy/pracownik/ban/{email}/{ban}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `setBanUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  setBanUzytkownik$Response(params: SetBanUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<Uzytkownik>> {
    return setBanUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `setBanUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  setBanUzytkownik(params: SetBanUzytkownik$Params, context?: HttpContext): Observable<Uzytkownik> {
    return this.setBanUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<Uzytkownik>): Uzytkownik => r.body)
    );
  }

  /** Path part for operation `updateAvatar()` */
  static readonly UpdateAvatarPath = '/rest/neo4j/uzytkownicy/avatar';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateAvatar()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateAvatar$Response(params?: UpdateAvatar$Params, context?: HttpContext): Observable<StrictHttpResponse<Uzytkownik>> {
    return updateAvatar(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateAvatar$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateAvatar(params?: UpdateAvatar$Params, context?: HttpContext): Observable<Uzytkownik> {
    return this.updateAvatar$Response(params, context).pipe(
      map((r: StrictHttpResponse<Uzytkownik>): Uzytkownik => r.body)
    );
  }

  /** Path part for operation `findAllUzytkownicy()` */
  static readonly FindAllUzytkownicyPath = '/rest/neo4j/uzytkownicy';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllUzytkownicy()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllUzytkownicy$Response(params?: FindAllUzytkownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Uzytkownik>>> {
    return findAllUzytkownicy(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllUzytkownicy$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllUzytkownicy(params?: FindAllUzytkownicy$Params, context?: HttpContext): Observable<Array<Uzytkownik>> {
    return this.findAllUzytkownicy$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<Uzytkownik>>): Array<Uzytkownik> => r.body)
    );
  }

  /** Path part for operation `removeSelf()` */
  static readonly RemoveSelfPath = '/rest/neo4j/uzytkownicy';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removeSelf()` instead.
   *
   * This method doesn't expect any request body.
   */
  removeSelf$Response(params?: RemoveSelf$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return removeSelf(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removeSelf$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  removeSelf(params?: RemoveSelf$Params, context?: HttpContext): Observable<void> {
    return this.removeSelf$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `getByEmail()` */
  static readonly GetByEmailPath = '/rest/neo4j/uzytkownicy/{email}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getByEmail()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByEmail$Response(params: GetByEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return getByEmail(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getByEmail$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByEmail(params: GetByEmail$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.getByEmail$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `remove()` */
  static readonly RemovePath = '/rest/neo4j/uzytkownicy/{email}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `remove()` instead.
   *
   * This method doesn't expect any request body.
   */
  remove$Response(params: Remove$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return remove(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `remove$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  remove(params: Remove$Params, context?: HttpContext): Observable<void> {
    return this.remove$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
