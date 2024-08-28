/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Komentarz } from '../../models/komentarz';
import { OcenaRequest } from '../../models/ocena-request';

export interface AddOcenaToKomentarz$Params {
      body: OcenaRequest
}

export function addOcenaToKomentarz(http: HttpClient, rootUrl: string, params: AddOcenaToKomentarz$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
  const rb = new RequestBuilder(rootUrl, addOcenaToKomentarz.PATH, 'put');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Komentarz>;
    })
  );
}

addOcenaToKomentarz.PATH = '/rest/neo4j/komentarze/oceny';
