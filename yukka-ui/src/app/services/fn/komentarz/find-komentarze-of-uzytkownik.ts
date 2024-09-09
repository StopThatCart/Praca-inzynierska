/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseKomentarzResponse } from '../../models/page-response-komentarz-response';

export interface FindKomentarzeOfUzytkownik$Params {
  page?: number;
  size?: number;
}

export function findKomentarzeOfUzytkownik(http: HttpClient, rootUrl: string, params?: FindKomentarzeOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseKomentarzResponse>> {
  const rb = new RequestBuilder(rootUrl, findKomentarzeOfUzytkownik.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseKomentarzResponse>;
    })
  );
}

findKomentarzeOfUzytkownik.PATH = '/komentarze/uzytkownicy';
