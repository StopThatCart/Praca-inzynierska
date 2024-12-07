/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addKomentarzToPost } from '../fn/komentarz/add-komentarz-to-post';
import { AddKomentarzToPost$Params } from '../fn/komentarz/add-komentarz-to-post';
import { addKomentarzToWiadomoscPrywatna } from '../fn/komentarz/add-komentarz-to-wiadomosc-prywatna';
import { AddKomentarzToWiadomoscPrywatna$Params } from '../fn/komentarz/add-komentarz-to-wiadomosc-prywatna';
import { addOcenaToKomentarz } from '../fn/komentarz/add-ocena-to-komentarz';
import { AddOcenaToKomentarz$Params } from '../fn/komentarz/add-ocena-to-komentarz';
import { addOdpowiedzToKomentarz } from '../fn/komentarz/add-odpowiedz-to-komentarz';
import { AddOdpowiedzToKomentarz$Params } from '../fn/komentarz/add-odpowiedz-to-komentarz';
import { findKomentarzById } from '../fn/komentarz/find-komentarz-by-id';
import { FindKomentarzById$Params } from '../fn/komentarz/find-komentarz-by-id';
import { findKomentarzeOfUzytkownik } from '../fn/komentarz/find-komentarze-of-uzytkownik';
import { FindKomentarzeOfUzytkownik$Params } from '../fn/komentarz/find-komentarze-of-uzytkownik';
import { KomentarzResponse } from '../models/komentarz-response';
import { PageResponseKomentarzResponse } from '../models/page-response-komentarz-response';
import { removeKomentarz } from '../fn/komentarz/remove-komentarz';
import { RemoveKomentarz$Params } from '../fn/komentarz/remove-komentarz';
import { removeOcenaFromKomentarz } from '../fn/komentarz/remove-ocena-from-komentarz';
import { RemoveOcenaFromKomentarz$Params } from '../fn/komentarz/remove-ocena-from-komentarz';
import { updateKomentarz } from '../fn/komentarz/update-komentarz';
import { UpdateKomentarz$Params } from '../fn/komentarz/update-komentarz';

@Injectable({ providedIn: 'root' })
export class KomentarzService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `addOcenaToKomentarz()` */
  static readonly AddOcenaToKomentarzPath = '/komentarze/oceny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOcenaToKomentarz()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOcenaToKomentarz$Response(params: AddOcenaToKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
    return addOcenaToKomentarz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOcenaToKomentarz$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOcenaToKomentarz(params: AddOcenaToKomentarz$Params, context?: HttpContext): Observable<KomentarzResponse> {
    return this.addOcenaToKomentarz$Response(params, context).pipe(
      map((r: StrictHttpResponse<KomentarzResponse>): KomentarzResponse => r.body)
    );
  }

  /** Path part for operation `removeOcenaFromKomentarz()` */
  static readonly RemoveOcenaFromKomentarzPath = '/komentarze/oceny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removeOcenaFromKomentarz()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  removeOcenaFromKomentarz$Response(params: RemoveOcenaFromKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return removeOcenaFromKomentarz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removeOcenaFromKomentarz$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  removeOcenaFromKomentarz(params: RemoveOcenaFromKomentarz$Params, context?: HttpContext): Observable<string> {
    return this.removeOcenaFromKomentarz$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `addKomentarzToWiadomoscPrywatna()` */
  static readonly AddKomentarzToWiadomoscPrywatnaPath = '/komentarze/wiadomosciPrywatne';

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

  /** Path part for operation `addKomentarzToPost()` */
  static readonly AddKomentarzToPostPath = '/komentarze/posty';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addKomentarzToPost()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToPost$Response(params?: AddKomentarzToPost$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
    return addKomentarzToPost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addKomentarzToPost$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToPost(params?: AddKomentarzToPost$Params, context?: HttpContext): Observable<KomentarzResponse> {
    return this.addKomentarzToPost$Response(params, context).pipe(
      map((r: StrictHttpResponse<KomentarzResponse>): KomentarzResponse => r.body)
    );
  }

  /** Path part for operation `addOdpowiedzToKomentarz()` */
  static readonly AddOdpowiedzToKomentarzPath = '/komentarze/odpowiedzi';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOdpowiedzToKomentarz()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addOdpowiedzToKomentarz$Response(params?: AddOdpowiedzToKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
    return addOdpowiedzToKomentarz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOdpowiedzToKomentarz$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addOdpowiedzToKomentarz(params?: AddOdpowiedzToKomentarz$Params, context?: HttpContext): Observable<KomentarzResponse> {
    return this.addOdpowiedzToKomentarz$Response(params, context).pipe(
      map((r: StrictHttpResponse<KomentarzResponse>): KomentarzResponse => r.body)
    );
  }

  /** Path part for operation `findKomentarzById()` */
  static readonly FindKomentarzByIdPath = '/komentarze/{komentarz-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findKomentarzById()` instead.
   *
   * This method doesn't expect any request body.
   */
  findKomentarzById$Response(params: FindKomentarzById$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
    return findKomentarzById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findKomentarzById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findKomentarzById(params: FindKomentarzById$Params, context?: HttpContext): Observable<KomentarzResponse> {
    return this.findKomentarzById$Response(params, context).pipe(
      map((r: StrictHttpResponse<KomentarzResponse>): KomentarzResponse => r.body)
    );
  }

  /** Path part for operation `removeKomentarz()` */
  static readonly RemoveKomentarzPath = '/komentarze/{komentarz-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removeKomentarz()` instead.
   *
   * This method doesn't expect any request body.
   */
  removeKomentarz$Response(params: RemoveKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return removeKomentarz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removeKomentarz$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  removeKomentarz(params: RemoveKomentarz$Params, context?: HttpContext): Observable<string> {
    return this.removeKomentarz$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `updateKomentarz()` */
  static readonly UpdateKomentarzPath = '/komentarze/{komentarz-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateKomentarz()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateKomentarz$Response(params: UpdateKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
    return updateKomentarz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateKomentarz$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateKomentarz(params: UpdateKomentarz$Params, context?: HttpContext): Observable<KomentarzResponse> {
    return this.updateKomentarz$Response(params, context).pipe(
      map((r: StrictHttpResponse<KomentarzResponse>): KomentarzResponse => r.body)
    );
  }

  /** Path part for operation `findKomentarzeOfUzytkownik()` */
  static readonly FindKomentarzeOfUzytkownikPath = '/komentarze/uzytkownicy/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findKomentarzeOfUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  findKomentarzeOfUzytkownik$Response(params: FindKomentarzeOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseKomentarzResponse>> {
    return findKomentarzeOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findKomentarzeOfUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findKomentarzeOfUzytkownik(params: FindKomentarzeOfUzytkownik$Params, context?: HttpContext): Observable<PageResponseKomentarzResponse> {
    return this.findKomentarzeOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseKomentarzResponse>): PageResponseKomentarzResponse => r.body)
    );
  }

}
