/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UzytkownikResponse } from '../../models/uzytkownik-response';

export interface FindByEmail$Params {
  email: string;
}

export function findByEmail(http: HttpClient, rootUrl: string, params: FindByEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
  const rb = new RequestBuilder(rootUrl, findByEmail.PATH, 'get');
  if (params) {
    rb.path('email', params.email, {});
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

findByEmail.PATH = '/uzytkownicy/email/{email}';
