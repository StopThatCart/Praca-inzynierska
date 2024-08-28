/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { deleteRoslinaFromDzialka } from '../fn/dzialka/delete-roslina-from-dzialka';
import { DeleteRoslinaFromDzialka$Params } from '../fn/dzialka/delete-roslina-from-dzialka';
import { deleteRoslinaObrazFromDzialka } from '../fn/dzialka/delete-roslina-obraz-from-dzialka';
import { DeleteRoslinaObrazFromDzialka$Params } from '../fn/dzialka/delete-roslina-obraz-from-dzialka';
import { DzialkaResponse } from '../models/dzialka-response';
import { getDzialkaByNumer } from '../fn/dzialka/get-dzialka-by-numer';
import { GetDzialkaByNumer$Params } from '../fn/dzialka/get-dzialka-by-numer';
import { getDzialkaOfUzytkownikByNumer } from '../fn/dzialka/get-dzialka-of-uzytkownik-by-numer';
import { GetDzialkaOfUzytkownikByNumer$Params } from '../fn/dzialka/get-dzialka-of-uzytkownik-by-numer';
import { getDzialki } from '../fn/dzialka/get-dzialki';
import { GetDzialki$Params } from '../fn/dzialka/get-dzialki';
import { getDzialkiOfUzytkownik } from '../fn/dzialka/get-dzialki-of-uzytkownik';
import { GetDzialkiOfUzytkownik$Params } from '../fn/dzialka/get-dzialki-of-uzytkownik';
import { saveRoslinaToDzialka } from '../fn/dzialka/save-roslina-to-dzialka';
import { SaveRoslinaToDzialka$Params } from '../fn/dzialka/save-roslina-to-dzialka';
import { updateRoslinaObrazInDzialka } from '../fn/dzialka/update-roslina-obraz-in-dzialka';
import { UpdateRoslinaObrazInDzialka$Params } from '../fn/dzialka/update-roslina-obraz-in-dzialka';
import { updateRoslinaPositionInDzialka } from '../fn/dzialka/update-roslina-position-in-dzialka';
import { UpdateRoslinaPositionInDzialka$Params } from '../fn/dzialka/update-roslina-position-in-dzialka';

@Injectable({ providedIn: 'root' })
export class DzialkaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `saveRoslinaToDzialka()` */
  static readonly SaveRoslinaToDzialkaPath = '/rest/neo4j/dzialki/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslinaToDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslinaToDzialka$Response(params: SaveRoslinaToDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return saveRoslinaToDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslinaToDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveRoslinaToDzialka(params: SaveRoslinaToDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.saveRoslinaToDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `deleteRoslinaFromDzialka()` */
  static readonly DeleteRoslinaFromDzialkaPath = '/rest/neo4j/dzialki/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteRoslinaFromDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  deleteRoslinaFromDzialka$Response(params: DeleteRoslinaFromDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return deleteRoslinaFromDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteRoslinaFromDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  deleteRoslinaFromDzialka(params: DeleteRoslinaFromDzialka$Params, context?: HttpContext): Observable<string> {
    return this.deleteRoslinaFromDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `updateRoslinaPositionInDzialka()` */
  static readonly UpdateRoslinaPositionInDzialkaPath = '/rest/neo4j/dzialki/rosliny/pozycja';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaPositionInDzialka()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaPositionInDzialka$Response(params: UpdateRoslinaPositionInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return updateRoslinaPositionInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaPositionInDzialka$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaPositionInDzialka(params: UpdateRoslinaPositionInDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.updateRoslinaPositionInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `deleteRoslinaObrazFromDzialka()` */
  static readonly DeleteRoslinaObrazFromDzialkaPath = '/rest/neo4j/dzialki/rosliny/obraz';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteRoslinaObrazFromDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  deleteRoslinaObrazFromDzialka$Response(params: DeleteRoslinaObrazFromDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return deleteRoslinaObrazFromDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteRoslinaObrazFromDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  deleteRoslinaObrazFromDzialka(params: DeleteRoslinaObrazFromDzialka$Params, context?: HttpContext): Observable<string> {
    return this.deleteRoslinaObrazFromDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `updateRoslinaObrazInDzialka()` */
  static readonly UpdateRoslinaObrazInDzialkaPath = '/rest/neo4j/dzialki/rosliny/obraz';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaObrazInDzialka()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObrazInDzialka$Response(params: UpdateRoslinaObrazInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return updateRoslinaObrazInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaObrazInDzialka$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObrazInDzialka(params: UpdateRoslinaObrazInDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.updateRoslinaObrazInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `getDzialki()` */
  static readonly GetDzialkiPath = '/rest/neo4j/dzialki';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getDzialki()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialki$Response(params?: GetDzialki$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DzialkaResponse>>> {
    return getDzialki(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getDzialki$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialki(params?: GetDzialki$Params, context?: HttpContext): Observable<Array<DzialkaResponse>> {
    return this.getDzialki$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<DzialkaResponse>>): Array<DzialkaResponse> => r.body)
    );
  }

  /** Path part for operation `getDzialkaByNumer()` */
  static readonly GetDzialkaByNumerPath = '/rest/neo4j/dzialki/{numer}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getDzialkaByNumer()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialkaByNumer$Response(params: GetDzialkaByNumer$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return getDzialkaByNumer(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getDzialkaByNumer$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialkaByNumer(params: GetDzialkaByNumer$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.getDzialkaByNumer$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `getDzialkaOfUzytkownikByNumer()` */
  static readonly GetDzialkaOfUzytkownikByNumerPath = '/rest/neo4j/dzialki/{numer}/uzytkownicy/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getDzialkaOfUzytkownikByNumer()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialkaOfUzytkownikByNumer$Response(params: GetDzialkaOfUzytkownikByNumer$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return getDzialkaOfUzytkownikByNumer(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getDzialkaOfUzytkownikByNumer$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialkaOfUzytkownikByNumer(params: GetDzialkaOfUzytkownikByNumer$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.getDzialkaOfUzytkownikByNumer$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `getDzialkiOfUzytkownik()` */
  static readonly GetDzialkiOfUzytkownikPath = '/rest/neo4j/dzialki/uzytkownicy/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getDzialkiOfUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialkiOfUzytkownik$Response(params: GetDzialkiOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DzialkaResponse>>> {
    return getDzialkiOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getDzialkiOfUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getDzialkiOfUzytkownik(params: GetDzialkiOfUzytkownik$Params, context?: HttpContext): Observable<Array<DzialkaResponse>> {
    return this.getDzialkiOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<DzialkaResponse>>): Array<DzialkaResponse> => r.body)
    );
  }

}
