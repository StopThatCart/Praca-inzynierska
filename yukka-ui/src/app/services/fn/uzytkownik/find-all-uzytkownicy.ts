/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseUzytkownikResponse } from '../../models/page-response-uzytkownik-response';

export interface FindAllUzytkownicy$Params {
  page?: number;
  size?: number;
  szukaj?: string;
}

export function findAllUzytkownicy(http: HttpClient, rootUrl: string, params?: FindAllUzytkownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseUzytkownikResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllUzytkownicy.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
    rb.query('szukaj', params.szukaj, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseUzytkownikResponse>;
    })
  );
}

findAllUzytkownicy.PATH = '/uzytkownicy/szukaj';
