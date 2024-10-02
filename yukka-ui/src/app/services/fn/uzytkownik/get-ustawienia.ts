/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UzytkownikResponse } from '../../models/uzytkownik-response';

export interface GetUstawienia$Params {
}

export function getUstawienia(http: HttpClient, rootUrl: string, params?: GetUstawienia$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
  const rb = new RequestBuilder(rootUrl, getUstawienia.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<UzytkownikResponse>;
    })
  );
}

getUstawienia.PATH = '/uzytkownicy/ustawienia';
