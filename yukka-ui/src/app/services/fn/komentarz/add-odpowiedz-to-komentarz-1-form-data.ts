/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Komentarz } from '../../models/komentarz';
import { KomentarzRequest } from '../../models/komentarz-request';

export interface AddOdpowiedzToKomentarz1$FormData$Params {
      body: {
'request'?: KomentarzRequest;
'file': Blob;
}
}

export function addOdpowiedzToKomentarz1$FormData(http: HttpClient, rootUrl: string, params: AddOdpowiedzToKomentarz1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<Komentarz>> {
  const rb = new RequestBuilder(rootUrl, addOdpowiedzToKomentarz1$FormData.PATH, 'post');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
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

addOdpowiedzToKomentarz1$FormData.PATH = '/rest/neo4j/komentarze/odpowiedzi';
