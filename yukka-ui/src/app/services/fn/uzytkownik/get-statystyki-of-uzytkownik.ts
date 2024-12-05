/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { StatystykiDto } from '../../models/statystyki-dto';

export interface GetStatystykiOfUzytkownik$Params {
  nazwa: string;
}

export function getStatystykiOfUzytkownik(http: HttpClient, rootUrl: string, params: GetStatystykiOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<StatystykiDto>> {
  const rb = new RequestBuilder(rootUrl, getStatystykiOfUzytkownik.PATH, 'get');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<StatystykiDto>;
    })
  );
}

getStatystykiOfUzytkownik.PATH = '/uzytkownicy/profil/{nazwa}';
