/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ProfilRequest } from '../../models/profil-request';
import { UzytkownikResponse } from '../../models/uzytkownik-response';

export interface UpdateProfil$Params {
      body?: {
'profil': ProfilRequest;
}
}

export function updateProfil(http: HttpClient, rootUrl: string, params?: UpdateProfil$Params, context?: HttpContext): Observable<StrictHttpResponse<UzytkownikResponse>> {
  const rb = new RequestBuilder(rootUrl, updateProfil.PATH, 'patch');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
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

updateProfil.PATH = '/uzytkownicy/profil';
