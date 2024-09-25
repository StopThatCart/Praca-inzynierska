/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DzialkaResponse } from '../../models/dzialka-response';

export interface GetDzialkaOfUzytkownikByNumer$Params {
  numer: number;
  'uzytkownik-nazwa': string;
}

export function getDzialkaOfUzytkownikByNumer(http: HttpClient, rootUrl: string, params: GetDzialkaOfUzytkownikByNumer$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
  const rb = new RequestBuilder(rootUrl, getDzialkaOfUzytkownikByNumer.PATH, 'get');
  if (params) {
    rb.path('numer', params.numer, {});
    rb.path('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<DzialkaResponse>;
    })
  );
}

getDzialkaOfUzytkownikByNumer.PATH = '/dzialki/{numer}/uzytkownicy/{uzytkownik-nazwa}';
