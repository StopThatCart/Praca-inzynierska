/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addKomentarzToPost1$FormData } from '../fn/komentarz/add-komentarz-to-post-1-form-data';
import { AddKomentarzToPost1$FormData$Params } from '../fn/komentarz/add-komentarz-to-post-1-form-data';
import { addKomentarzToPost1$Json } from '../fn/komentarz/add-komentarz-to-post-1-json';
import { AddKomentarzToPost1$Json$Params } from '../fn/komentarz/add-komentarz-to-post-1-json';
import { addKomentarzToWiadomoscPrywatna1$FormData } from '../fn/komentarz/add-komentarz-to-wiadomosc-prywatna-1-form-data';
import { AddKomentarzToWiadomoscPrywatna1$FormData$Params } from '../fn/komentarz/add-komentarz-to-wiadomosc-prywatna-1-form-data';
import { addKomentarzToWiadomoscPrywatna1$Json } from '../fn/komentarz/add-komentarz-to-wiadomosc-prywatna-1-json';
import { AddKomentarzToWiadomoscPrywatna1$Json$Params } from '../fn/komentarz/add-komentarz-to-wiadomosc-prywatna-1-json';
import { addOcenaToKomentarz } from '../fn/komentarz/add-ocena-to-komentarz';
import { AddOcenaToKomentarz$Params } from '../fn/komentarz/add-ocena-to-komentarz';
import { addOdpowiedzToKomentarz1$FormData$Any } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-form-data-any';
import { AddOdpowiedzToKomentarz1$FormData$Any$Params } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-form-data-any';
import { addOdpowiedzToKomentarz1$FormData$Json } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-form-data-json';
import { AddOdpowiedzToKomentarz1$FormData$Json$Params } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-form-data-json';
import { addOdpowiedzToKomentarz1$Json$Any } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-json-any';
import { AddOdpowiedzToKomentarz1$Json$Any$Params } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-json-any';
import { addOdpowiedzToKomentarz1$Json$Json } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-json-json';
import { AddOdpowiedzToKomentarz1$Json$Json$Params } from '../fn/komentarz/add-odpowiedz-to-komentarz-1-json-json';
import { findKomentarzById } from '../fn/komentarz/find-komentarz-by-id';
import { FindKomentarzById$Params } from '../fn/komentarz/find-komentarz-by-id';
import { findKomentarzeOfUzytkownik } from '../fn/komentarz/find-komentarze-of-uzytkownik';
import { FindKomentarzeOfUzytkownik$Params } from '../fn/komentarz/find-komentarze-of-uzytkownik';
import { Komentarz } from '../models/komentarz';
import { KomentarzResponse } from '../models/komentarz-response';
import { PageResponseKomentarzResponse } from '../models/page-response-komentarz-response';
import { removeKomentarz } from '../fn/komentarz/remove-komentarz';
import { RemoveKomentarz$Params } from '../fn/komentarz/remove-komentarz';
import { removeKomentarzFromPost } from '../fn/komentarz/remove-komentarz-from-post';
import { RemoveKomentarzFromPost$Params } from '../fn/komentarz/remove-komentarz-from-post';
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
  addOcenaToKomentarz$Response(params: AddOcenaToKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addOcenaToKomentarz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOcenaToKomentarz$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOcenaToKomentarz(params: AddOcenaToKomentarz$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addOcenaToKomentarz$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
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

  /** Path part for operation `addKomentarzToWiadomoscPrywatna1()` */
  static readonly AddKomentarzToWiadomoscPrywatna1Path = '/komentarze/wiadomosciPrywatne/{other-uzyt-nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addKomentarzToWiadomoscPrywatna1$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addKomentarzToWiadomoscPrywatna1$Json$Response(params: AddKomentarzToWiadomoscPrywatna1$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addKomentarzToWiadomoscPrywatna1$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addKomentarzToWiadomoscPrywatna1$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addKomentarzToWiadomoscPrywatna1$Json(params: AddKomentarzToWiadomoscPrywatna1$Json$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addKomentarzToWiadomoscPrywatna1$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addKomentarzToWiadomoscPrywatna1$FormData()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToWiadomoscPrywatna1$FormData$Response(params: AddKomentarzToWiadomoscPrywatna1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addKomentarzToWiadomoscPrywatna1$FormData(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addKomentarzToWiadomoscPrywatna1$FormData$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToWiadomoscPrywatna1$FormData(params: AddKomentarzToWiadomoscPrywatna1$FormData$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addKomentarzToWiadomoscPrywatna1$FormData$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /** Path part for operation `addKomentarzToPost1()` */
  static readonly AddKomentarzToPost1Path = '/komentarze/posty';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addKomentarzToPost1$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addKomentarzToPost1$Json$Response(params: AddKomentarzToPost1$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addKomentarzToPost1$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addKomentarzToPost1$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addKomentarzToPost1$Json(params: AddKomentarzToPost1$Json$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addKomentarzToPost1$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addKomentarzToPost1$FormData()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToPost1$FormData$Response(params: AddKomentarzToPost1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addKomentarzToPost1$FormData(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addKomentarzToPost1$FormData$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addKomentarzToPost1$FormData(params: AddKomentarzToPost1$FormData$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addKomentarzToPost1$FormData$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /** Path part for operation `addOdpowiedzToKomentarz1()` */
  static readonly AddOdpowiedzToKomentarz1Path = '/komentarze/odpowiedzi';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOdpowiedzToKomentarz1$Json$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOdpowiedzToKomentarz1$Json$Json$Response(params: AddOdpowiedzToKomentarz1$Json$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addOdpowiedzToKomentarz1$Json$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOdpowiedzToKomentarz1$Json$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOdpowiedzToKomentarz1$Json$Json(params: AddOdpowiedzToKomentarz1$Json$Json$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addOdpowiedzToKomentarz1$Json$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOdpowiedzToKomentarz1$Json$Any()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOdpowiedzToKomentarz1$Json$Any$Response(params: AddOdpowiedzToKomentarz1$Json$Any$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addOdpowiedzToKomentarz1$Json$Any(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOdpowiedzToKomentarz1$Json$Any$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOdpowiedzToKomentarz1$Json$Any(params: AddOdpowiedzToKomentarz1$Json$Any$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addOdpowiedzToKomentarz1$Json$Any$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOdpowiedzToKomentarz1$FormData$Json()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addOdpowiedzToKomentarz1$FormData$Json$Response(params: AddOdpowiedzToKomentarz1$FormData$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addOdpowiedzToKomentarz1$FormData$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOdpowiedzToKomentarz1$FormData$Json$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addOdpowiedzToKomentarz1$FormData$Json(params: AddOdpowiedzToKomentarz1$FormData$Json$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addOdpowiedzToKomentarz1$FormData$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOdpowiedzToKomentarz1$FormData$Any()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addOdpowiedzToKomentarz1$FormData$Any$Response(params: AddOdpowiedzToKomentarz1$FormData$Any$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return addOdpowiedzToKomentarz1$FormData$Any(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOdpowiedzToKomentarz1$FormData$Any$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addOdpowiedzToKomentarz1$FormData$Any(params: AddOdpowiedzToKomentarz1$FormData$Any$Params, context?: HttpContext): Observable<Komentarz> {
    return this.addOdpowiedzToKomentarz1$FormData$Any$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
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
  updateKomentarz$Response(params: UpdateKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
    return updateKomentarz(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateKomentarz$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateKomentarz(params: UpdateKomentarz$Params, context?: HttpContext): Observable<Komentarz> {
    return this.updateKomentarz$Response(params, context).pipe(
      map((r: StrictHttpResponse<Komentarz>): Komentarz => r.body)
    );
  }

  /** Path part for operation `findKomentarzeOfUzytkownik()` */
  static readonly FindKomentarzeOfUzytkownikPath = '/komentarze/uzytkownicy';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findKomentarzeOfUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  findKomentarzeOfUzytkownik$Response(params?: FindKomentarzeOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseKomentarzResponse>> {
    return findKomentarzeOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findKomentarzeOfUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findKomentarzeOfUzytkownik(params?: FindKomentarzeOfUzytkownik$Params, context?: HttpContext): Observable<PageResponseKomentarzResponse> {
    return this.findKomentarzeOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseKomentarzResponse>): PageResponseKomentarzResponse => r.body)
    );
  }

  /** Path part for operation `removeKomentarzFromPost()` */
  static readonly RemoveKomentarzFromPostPath = '/komentarze/{komentarz-id}/posty/{post-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removeKomentarzFromPost()` instead.
   *
   * This method doesn't expect any request body.
   */
  removeKomentarzFromPost$Response(params: RemoveKomentarzFromPost$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return removeKomentarzFromPost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removeKomentarzFromPost$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  removeKomentarzFromPost(params: RemoveKomentarzFromPost$Params, context?: HttpContext): Observable<string> {
    return this.removeKomentarzFromPost$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
