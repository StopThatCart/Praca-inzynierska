/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UzytkownikResponse } from '../../models/uzytkownik-response';

export interface FindByNazwa$Params {
  nazwa: string;
}

export function findByNazwa(http: HttpClient, rootUrl: string, params: FindByNazwa$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
  const rb = new RequestBuilder(rootUrl, findByNazwa.PATH, 'get');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
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

findByNazwa.PATH = '/uzytkownicy/{nazwa}';
