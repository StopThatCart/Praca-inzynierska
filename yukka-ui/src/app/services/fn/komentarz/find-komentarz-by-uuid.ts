/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { KomentarzResponse } from '../../models/komentarz-response';

export interface FindKomentarzByUuid$Params {
  uuid: string;
}

export function findKomentarzByUuid(http: HttpClient, rootUrl: string, params: FindKomentarzByUuid$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
  const rb = new RequestBuilder(rootUrl, findKomentarzByUuid.PATH, 'get');
  if (params) {
    rb.path('uuid', params.uuid, {});
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

findKomentarzByUuid.PATH = '/komentarze/{uuid}';
