/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UzytkownikResponse } from '../../models/uzytkownik-response';

export interface GetByEmail$Params {
  email: string;
}

export function getByEmail(http: HttpClient, rootUrl: string, params: GetByEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
  const rb = new RequestBuilder(rootUrl, getByEmail.PATH, 'get');
  if (params) {
    rb.path('email', params.email, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<UzytkownikResponse>;
    })
  );
}

getByEmail.PATH = '/rest/neo4j/uzytkownicy/{email}';
