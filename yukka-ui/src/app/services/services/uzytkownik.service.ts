/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { FileResponse } from '../models/file-response';
import { findAllUzytkownicy } from '../fn/uzytkownik/find-all-uzytkownicy';
import { FindAllUzytkownicy$Params } from '../fn/uzytkownik/find-all-uzytkownicy';
import { findByEmail } from '../fn/uzytkownik/find-by-email';
import { FindByEmail$Params } from '../fn/uzytkownik/find-by-email';
import { findByNazwa } from '../fn/uzytkownik/find-by-nazwa';
import { FindByNazwa$Params } from '../fn/uzytkownik/find-by-nazwa';
import { getAvatar } from '../fn/uzytkownik/get-avatar';
import { GetAvatar$Params } from '../fn/uzytkownik/get-avatar';
import { getBlokowaniAndBlokujacy } from '../fn/uzytkownik/get-blokowani-and-blokujacy';
import { GetBlokowaniAndBlokujacy$Params } from '../fn/uzytkownik/get-blokowani-and-blokujacy';
import { getStatystykiOfUzytkownik } from '../fn/uzytkownik/get-statystyki-of-uzytkownik';
import { GetStatystykiOfUzytkownik$Params } from '../fn/uzytkownik/get-statystyki-of-uzytkownik';
import { getUstawienia } from '../fn/uzytkownik/get-ustawienia';
import { GetUstawienia$Params } from '../fn/uzytkownik/get-ustawienia';
import { PageResponseUzytkownikResponse } from '../models/page-response-uzytkownik-response';
import { removeSelf } from '../fn/uzytkownik/remove-self';
import { RemoveSelf$Params } from '../fn/uzytkownik/remove-self';
import { sendChangeEmail } from '../fn/uzytkownik/send-change-email';
import { SendChangeEmail$Params } from '../fn/uzytkownik/send-change-email';
import { setBlokUzytkownik } from '../fn/uzytkownik/set-blok-uzytkownik';
import { SetBlokUzytkownik$Params } from '../fn/uzytkownik/set-blok-uzytkownik';
import { StatystykiDto } from '../models/statystyki-dto';
import { updateAvatar } from '../fn/uzytkownik/update-avatar';
import { UpdateAvatar$Params } from '../fn/uzytkownik/update-avatar';
import { updateProfil } from '../fn/uzytkownik/update-profil';
import { UpdateProfil$Params } from '../fn/uzytkownik/update-profil';
import { updateUstawienia } from '../fn/uzytkownik/update-ustawienia';
import { UpdateUstawienia$Params } from '../fn/uzytkownik/update-ustawienia';
import { UzytkownikResponse } from '../models/uzytkownik-response';

@Injectable({ providedIn: 'root' })
export class UzytkownikService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `sendChangeEmail()` */
  static readonly SendChangeEmailPath = '/uzytkownicy/send-zmiana-email';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `sendChangeEmail()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendChangeEmail$Response(params: SendChangeEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return sendChangeEmail(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `sendChangeEmail$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  sendChangeEmail(params: SendChangeEmail$Params, context?: HttpContext): Observable<{
}> {
    return this.sendChangeEmail$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `getUstawienia()` */
  static readonly GetUstawieniaPath = '/uzytkownicy/ustawienia';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getUstawienia()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUstawienia$Response(params?: GetUstawienia$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return getUstawienia(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getUstawienia$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUstawienia(params?: GetUstawienia$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.getUstawienia$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `updateUstawienia()` */
  static readonly UpdateUstawieniaPath = '/uzytkownicy/ustawienia';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateUstawienia()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateUstawienia$Response(params?: UpdateUstawienia$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return updateUstawienia(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateUstawienia$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateUstawienia(params?: UpdateUstawienia$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.updateUstawienia$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `updateProfil()` */
  static readonly UpdateProfilPath = '/uzytkownicy/profil';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateProfil()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateProfil$Response(params?: UpdateProfil$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return updateProfil(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateProfil$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateProfil(params?: UpdateProfil$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.updateProfil$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `setBlokUzytkownik()` */
  static readonly SetBlokUzytkownikPath = '/uzytkownicy/blok/{nazwa}/{blok}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `setBlokUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  setBlokUzytkownik$Response(params: SetBlokUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return setBlokUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `setBlokUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  setBlokUzytkownik(params: SetBlokUzytkownik$Params, context?: HttpContext): Observable<boolean> {
    return this.setBlokUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
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
  getAvatar$Response(params?: GetAvatar$Params, context?: HttpContext): Observable<StrictHttpResponse<FileResponse>> {
    return getAvatar(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAvatar$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAvatar(params?: GetAvatar$Params, context?: HttpContext): Observable<FileResponse> {
    return this.getAvatar$Response(params, context).pipe(
      map((r: StrictHttpResponse<FileResponse>): FileResponse => r.body)
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
  static readonly FindAllUzytkownicyPath = '/uzytkownicy/szukaj';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllUzytkownicy()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllUzytkownicy$Response(params?: FindAllUzytkownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseUzytkownikResponse>> {
    return findAllUzytkownicy(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllUzytkownicy$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllUzytkownicy(params?: FindAllUzytkownicy$Params, context?: HttpContext): Observable<PageResponseUzytkownikResponse> {
    return this.findAllUzytkownicy$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseUzytkownikResponse>): PageResponseUzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `getStatystykiOfUzytkownik()` */
  static readonly GetStatystykiOfUzytkownikPath = '/uzytkownicy/profil/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getStatystykiOfUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  getStatystykiOfUzytkownik$Response(params: GetStatystykiOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<StatystykiDto>> {
    return getStatystykiOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getStatystykiOfUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getStatystykiOfUzytkownik(params: GetStatystykiOfUzytkownik$Params, context?: HttpContext): Observable<StatystykiDto> {
    return this.getStatystykiOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<StatystykiDto>): StatystykiDto => r.body)
    );
  }

  /** Path part for operation `findByNazwa()` */
  static readonly FindByNazwaPath = '/uzytkownicy/nazwa/{nazwa}';

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
  static readonly FindByEmailPath = '/uzytkownicy/email/{email}';

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

  /** Path part for operation `getBlokowaniAndBlokujacy()` */
  static readonly GetBlokowaniAndBlokujacyPath = '/uzytkownicy/blokowani';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getBlokowaniAndBlokujacy()` instead.
   *
   * This method doesn't expect any request body.
   */
  getBlokowaniAndBlokujacy$Response(params?: GetBlokowaniAndBlokujacy$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
    return getBlokowaniAndBlokujacy(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getBlokowaniAndBlokujacy$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getBlokowaniAndBlokujacy(params?: GetBlokowaniAndBlokujacy$Params, context?: HttpContext): Observable<UzytkownikResponse> {
    return this.getBlokowaniAndBlokujacy$Response(params, context).pipe(
      map((r: StrictHttpResponse<UzytkownikResponse>): UzytkownikResponse => r.body)
    );
  }

  /** Path part for operation `removeSelf()` */
  static readonly RemoveSelfPath = '/uzytkownicy';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removeSelf()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  removeSelf$Response(params: RemoveSelf$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return removeSelf(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removeSelf$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  removeSelf(params: RemoveSelf$Params, context?: HttpContext): Observable<void> {
    return this.removeSelf$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
