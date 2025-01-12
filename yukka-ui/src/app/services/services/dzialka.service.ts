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
import { deleteRoslinaTeksturaFromDzialka } from '../fn/dzialka/delete-roslina-tekstura-from-dzialka';
import { DeleteRoslinaTeksturaFromDzialka$Params } from '../fn/dzialka/delete-roslina-tekstura-from-dzialka';
import { DzialkaResponse } from '../models/dzialka-response';
import { FileResponse } from '../models/file-response';
import { getDzialkaOfUzytkownikByNumer } from '../fn/dzialka/get-dzialka-of-uzytkownik-by-numer';
import { GetDzialkaOfUzytkownikByNumer$Params } from '../fn/dzialka/get-dzialka-of-uzytkownik-by-numer';
import { getPozycjeInDzialki } from '../fn/dzialka/get-pozycje-in-dzialki';
import { GetPozycjeInDzialki$Params } from '../fn/dzialka/get-pozycje-in-dzialki';
import { getRoslinaInDzialka } from '../fn/dzialka/get-roslina-in-dzialka';
import { GetRoslinaInDzialka$Params } from '../fn/dzialka/get-roslina-in-dzialka';
import { getRoslinaInDzialkaByUuid } from '../fn/dzialka/get-roslina-in-dzialka-by-uuid';
import { GetRoslinaInDzialkaByUuid$Params } from '../fn/dzialka/get-roslina-in-dzialka-by-uuid';
import { renameDzialka } from '../fn/dzialka/rename-dzialka';
import { RenameDzialka$Params } from '../fn/dzialka/rename-dzialka';
import { saveRoslinaToDzialka } from '../fn/dzialka/save-roslina-to-dzialka';
import { SaveRoslinaToDzialka$Params } from '../fn/dzialka/save-roslina-to-dzialka';
import { updateRoslinaKolorInDzialka } from '../fn/dzialka/update-roslina-kolor-in-dzialka';
import { UpdateRoslinaKolorInDzialka$Params } from '../fn/dzialka/update-roslina-kolor-in-dzialka';
import { updateRoslinaNotatkaInDzialka } from '../fn/dzialka/update-roslina-notatka-in-dzialka';
import { UpdateRoslinaNotatkaInDzialka$Params } from '../fn/dzialka/update-roslina-notatka-in-dzialka';
import { updateRoslinaObrazInDzialka } from '../fn/dzialka/update-roslina-obraz-in-dzialka';
import { UpdateRoslinaObrazInDzialka$Params } from '../fn/dzialka/update-roslina-obraz-in-dzialka';
import { updateRoslinaPozycjaInDzialka } from '../fn/dzialka/update-roslina-pozycja-in-dzialka';
import { UpdateRoslinaPozycjaInDzialka$Params } from '../fn/dzialka/update-roslina-pozycja-in-dzialka';
import { updateRoslinaWyswietlanieInDzialka } from '../fn/dzialka/update-roslina-wyswietlanie-in-dzialka';
import { UpdateRoslinaWyswietlanieInDzialka$Params } from '../fn/dzialka/update-roslina-wyswietlanie-in-dzialka';
import { ZasadzonaRoslinaResponse } from '../models/zasadzona-roslina-response';

@Injectable({ providedIn: 'root' })
export class DzialkaService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `saveRoslinaToDzialka()` */
  static readonly SaveRoslinaToDzialkaPath = '/dzialki/rosliny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveRoslinaToDzialka()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslinaToDzialka$Response(params?: SaveRoslinaToDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return saveRoslinaToDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveRoslinaToDzialka$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  saveRoslinaToDzialka(params?: SaveRoslinaToDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.saveRoslinaToDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `deleteRoslinaFromDzialka()` */
  static readonly DeleteRoslinaFromDzialkaPath = '/dzialki/rosliny';

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

  /** Path part for operation `renameDzialka()` */
  static readonly RenameDzialkaPath = '/dzialki/{numer}/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `renameDzialka()` instead.
   *
   * This method doesn't expect any request body.
   */
  renameDzialka$Response(params: RenameDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return renameDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `renameDzialka$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  renameDzialka(params: RenameDzialka$Params, context?: HttpContext): Observable<string> {
    return this.renameDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `updateRoslinaWyswietlanieInDzialka()` */
  static readonly UpdateRoslinaWyswietlanieInDzialkaPath = '/dzialki/rosliny/wyswietlanie';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaWyswietlanieInDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaWyswietlanieInDzialka$Response(params: UpdateRoslinaWyswietlanieInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return updateRoslinaWyswietlanieInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaWyswietlanieInDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaWyswietlanieInDzialka(params: UpdateRoslinaWyswietlanieInDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.updateRoslinaWyswietlanieInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `updateRoslinaPozycjaInDzialka()` */
  static readonly UpdateRoslinaPozycjaInDzialkaPath = '/dzialki/rosliny/pozycja';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaPozycjaInDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaPozycjaInDzialka$Response(params: UpdateRoslinaPozycjaInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return updateRoslinaPozycjaInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaPozycjaInDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaPozycjaInDzialka(params: UpdateRoslinaPozycjaInDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.updateRoslinaPozycjaInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `deleteRoslinaObrazFromDzialka()` */
  static readonly DeleteRoslinaObrazFromDzialkaPath = '/dzialki/rosliny/obraz';

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
  static readonly UpdateRoslinaObrazInDzialkaPath = '/dzialki/rosliny/obraz';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaObrazInDzialka()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObrazInDzialka$Response(params?: UpdateRoslinaObrazInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<FileResponse>> {
    return updateRoslinaObrazInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaObrazInDzialka$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  updateRoslinaObrazInDzialka(params?: UpdateRoslinaObrazInDzialka$Params, context?: HttpContext): Observable<FileResponse> {
    return this.updateRoslinaObrazInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<FileResponse>): FileResponse => r.body)
    );
  }

  /** Path part for operation `updateRoslinaNotatkaInDzialka()` */
  static readonly UpdateRoslinaNotatkaInDzialkaPath = '/dzialki/rosliny/notatka';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaNotatkaInDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaNotatkaInDzialka$Response(params: UpdateRoslinaNotatkaInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return updateRoslinaNotatkaInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaNotatkaInDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaNotatkaInDzialka(params: UpdateRoslinaNotatkaInDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.updateRoslinaNotatkaInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `updateRoslinaKolorInDzialka()` */
  static readonly UpdateRoslinaKolorInDzialkaPath = '/dzialki/rosliny/kolor';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateRoslinaKolorInDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaKolorInDzialka$Response(params: UpdateRoslinaKolorInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
    return updateRoslinaKolorInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateRoslinaKolorInDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateRoslinaKolorInDzialka(params: UpdateRoslinaKolorInDzialka$Params, context?: HttpContext): Observable<DzialkaResponse> {
    return this.updateRoslinaKolorInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<DzialkaResponse>): DzialkaResponse => r.body)
    );
  }

  /** Path part for operation `getRoslinaInDzialka()` */
  static readonly GetRoslinaInDzialkaPath = '/dzialki/{numer}/{x}/{y}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getRoslinaInDzialka()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRoslinaInDzialka$Response(params: GetRoslinaInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<ZasadzonaRoslinaResponse>> {
    return getRoslinaInDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getRoslinaInDzialka$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRoslinaInDzialka(params: GetRoslinaInDzialka$Params, context?: HttpContext): Observable<ZasadzonaRoslinaResponse> {
    return this.getRoslinaInDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<ZasadzonaRoslinaResponse>): ZasadzonaRoslinaResponse => r.body)
    );
  }

  /** Path part for operation `getRoslinaInDzialkaByUuid()` */
  static readonly GetRoslinaInDzialkaByUuidPath = '/dzialki/{numer}/{uuid}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getRoslinaInDzialkaByUuid()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRoslinaInDzialkaByUuid$Response(params: GetRoslinaInDzialkaByUuid$Params, context?: HttpContext): Observable<StrictHttpResponse<ZasadzonaRoslinaResponse>> {
    return getRoslinaInDzialkaByUuid(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getRoslinaInDzialkaByUuid$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getRoslinaInDzialkaByUuid(params: GetRoslinaInDzialkaByUuid$Params, context?: HttpContext): Observable<ZasadzonaRoslinaResponse> {
    return this.getRoslinaInDzialkaByUuid$Response(params, context).pipe(
      map((r: StrictHttpResponse<ZasadzonaRoslinaResponse>): ZasadzonaRoslinaResponse => r.body)
    );
  }

  /** Path part for operation `getDzialkaOfUzytkownikByNumer()` */
  static readonly GetDzialkaOfUzytkownikByNumerPath = '/dzialki/{numer}/uzytkownicy/{uzytkownik-nazwa}';

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

  /** Path part for operation `getPozycjeInDzialki()` */
  static readonly GetPozycjeInDzialkiPath = '/dzialki/pozycje';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getPozycjeInDzialki()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPozycjeInDzialki$Response(params?: GetPozycjeInDzialki$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DzialkaResponse>>> {
    return getPozycjeInDzialki(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getPozycjeInDzialki$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPozycjeInDzialki(params?: GetPozycjeInDzialki$Params, context?: HttpContext): Observable<Array<DzialkaResponse>> {
    return this.getPozycjeInDzialki$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<DzialkaResponse>>): Array<DzialkaResponse> => r.body)
    );
  }

  /** Path part for operation `deleteRoslinaTeksturaFromDzialka()` */
  static readonly DeleteRoslinaTeksturaFromDzialkaPath = '/dzialki/rosliny/tekstura';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteRoslinaTeksturaFromDzialka()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  deleteRoslinaTeksturaFromDzialka$Response(params: DeleteRoslinaTeksturaFromDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return deleteRoslinaTeksturaFromDzialka(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteRoslinaTeksturaFromDzialka$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  deleteRoslinaTeksturaFromDzialka(params: DeleteRoslinaTeksturaFromDzialka$Params, context?: HttpContext): Observable<string> {
    return this.deleteRoslinaTeksturaFromDzialka$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
