/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Komentarz } from '../../models/komentarz';
import { KomentarzRequest } from '../../models/komentarz-request';

export interface AddKomentarzToPost1$Json$Params {
      body: KomentarzRequest
}

export function addKomentarzToPost1$Json(http: HttpClient, rootUrl: string, params: AddKomentarzToPost1$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
  const rb = new RequestBuilder(rootUrl, addKomentarzToPost1$Json.PATH, 'post');
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

addKomentarzToPost1$Json.PATH = '/rest/neo4j/komentarze/posty';
