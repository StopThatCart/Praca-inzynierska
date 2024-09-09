/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Komentarz } from '../../models/komentarz';
import { KomentarzRequest } from '../../models/komentarz-request';

export interface UpdateKomentarz$Params {
  'komentarz-id': string;
      body: KomentarzRequest
}

export function updateKomentarz(http: HttpClient, rootUrl: string, params: UpdateKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
  const rb = new RequestBuilder(rootUrl, updateKomentarz.PATH, 'patch');
  if (params) {
    rb.path('komentarz-id', params['komentarz-id'], {});
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Komentarz>;
    })
  );
}

updateKomentarz.PATH = '/komentarze/{komentarz-id}';
