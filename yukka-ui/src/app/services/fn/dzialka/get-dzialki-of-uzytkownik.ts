/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DzialkaResponse } from '../../models/dzialka-response';

export interface GetDzialkiOfUzytkownik$Params {
  nazwa: string;
}

export function getDzialkiOfUzytkownik(http: HttpClient, rootUrl: string, params: GetDzialkiOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DzialkaResponse>>> {
  const rb = new RequestBuilder(rootUrl, getDzialkiOfUzytkownik.PATH, 'get');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<DzialkaResponse>>;
    })
  );
}

getDzialkiOfUzytkownik.PATH = '/rest/neo4j/dzialki/uzytkownicy/{nazwa}';
