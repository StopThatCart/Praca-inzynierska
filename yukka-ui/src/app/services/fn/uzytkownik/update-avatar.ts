/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Uzytkownik } from '../../models/uzytkownik';

export interface UpdateAvatar$Params {
      body?: {
'file': Blob;
}
}

export function updateAvatar(http: HttpClient, rootUrl: string, params?: UpdateAvatar$Params, context?: HttpContext): Observable<StrictHttpResponse<Uzytkownik>> {
  const rb = new RequestBuilder(rootUrl, updateAvatar.PATH, 'patch');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Uzytkownik>;
    })
  );
}

updateAvatar.PATH = '/rest/neo4j/uzytkownicy/avatar';
