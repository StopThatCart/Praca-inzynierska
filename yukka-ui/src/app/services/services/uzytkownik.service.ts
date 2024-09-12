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
import { findByEmail } from '../fn/uzytkownik/find-by-email';
import { FindByEmail$Params } from '../fn/uzytkownik/find-by-email';
import { findByNazwa } from '../fn/uzytkownik/find-by-nazwa';
import { FindByNazwa$Params } from '../fn/uzytkownik/find-by-nazwa';
import { getAvatar } from '../fn/uzytkownik/get-avatar';
import { GetAvatar$Params } from '../fn/uzytkownik/get-avatar';
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
import { setPowiadomieniePrzeczytane } from '../fn/uzytkownik/set-powiadomienie-przeczytane';
import { SetPowiadomieniePrzeczytane$Params } from '../fn/uzytkownik/set-powiadomienie-przeczytane';
import { updateAvatar } from '../fn/uzytkownik/update-avatar';
import { UpdateAvatar$Params } from '../fn/uzytkownik/update-avatar';
import { Uzytkownik } from '../models/uzytkownik';
import { UzytkownikResponse } from '../models/uzytkownik-response';

@Injectable({ providedIn: 'root' })
export class UzytkownikService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `setPowiadomieniePrzeczytane()` */
  static readonly SetPowiadomieniePrzeczytanePath = '/uzytkownicy/{nazwa}/powiadomienie/{id}/przeczytane';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `setPowiadomieniePrzeczytane()` instead.
   *
   * This method doesn't expect any request body.
   */
  setPowiadomieniePrzeczytane$Response(params: SetPowiadomieniePrzeczytane$Params, context?: HttpContext): Observable<StrictHttpResponse<Powiadomienie>> {
    return setPowiadomieniePrzeczytane(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `setPowiadomieniePrzeczytane$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  setPowiadomieniePrzeczytane(params: SetPowiadomieniePrzeczytane$Params, context?: HttpContext): Observable<Powiadomienie> {
    return this.setPowiadomieniePrzeczytane$Response(params, context).pipe(
      map((r: StrictHttpResponse<Powiadomienie>): Powiadomienie => r.body)
    );
  }

  /** Path part for operation `sendSpecjalnePowiadomienie()` */
  static readonly SendSpecjalnePowiadomieniePath = '/uzytkownicy/pracownik/powiadomienie';

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
  static readonly SendSpecjalnePowiadomienieToPracownicyPath = '/uzytkownicy/admin/powiadomienie';

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
  static readonly SetBanUzytkownikPath = '/uzytkownicy/pracownik/ban/{email}/{ban}';

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

  /** Path part for operation `getAvatar()` */
  static readonly GetAvatarPath = '/uzytkownicy/avatar';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAvatar()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAvatar$Response(params?: GetAvatar$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return getAvatar(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAvatar$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAvatar(params?: GetAvatar$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.getAvatar$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `updateAvatar()` */
  static readonly UpdateAvatarPath = '/uzytkownicy/avatar';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateAvatar()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateAvatar$Response(params?: UpdateAvatar$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return updateAvatar(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateAvatar$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateAvatar(params?: UpdateAvatar$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.updateAvatar$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `findAllUzytkownicy()` */
  static readonly FindAllUzytkownicyPath = '/uzytkownicy';

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
  static readonly RemoveSelfPath = '/uzytkownicy';

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

  /** Path part for operation `findByNazwa()` */
  static readonly FindByNazwaPath = '/uzytkownicy/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByNazwa()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByNazwa$Response(params: FindByNazwa$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return findByNazwa(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByNazwa$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByNazwa(params: FindByNazwa$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.findByNazwa$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `findByEmail()` */
  static readonly FindByEmailPath = '/uzytkownicy/{email}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByEmail()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByEmail$Response(params: FindByEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return findByEmail(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByEmail$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByEmail(params: FindByEmail$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.findByEmail$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `remove()` */
  static readonly RemovePath = '/uzytkownicy/{email}';

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
