/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { KomentarzResponse } from '../../models/komentarz-response';

export interface FindKomentarzById$Params {
  'komentarz-id': string;
}

export function findKomentarzById(http: HttpClient, rootUrl: string, params: FindKomentarzById$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
  const rb = new RequestBuilder(rootUrl, findKomentarzById.PATH, 'get');
  if (params) {
    rb.path('komentarz-id', params['komentarz-id'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<KomentarzResponse>;
    })
  );
}

findKomentarzById.PATH = '/komentarze/{komentarz-id}';
