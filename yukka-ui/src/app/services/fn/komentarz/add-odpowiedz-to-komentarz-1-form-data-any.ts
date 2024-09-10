/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { KomentarzRequest } from '../../models/komentarz-request';
import { KomentarzResponse } from '../../models/komentarz-response';

export interface AddOdpowiedzToKomentarz1$FormData$Any$Params {
      body: {
'request': KomentarzRequest;
'file': Blob;
}
}

export function addOdpowiedzToKomentarz1$FormData$Any(http: HttpClient, rootUrl: string, params: AddOdpowiedzToKomentarz1$FormData$Any$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
  const rb = new RequestBuilder(rootUrl, addOdpowiedzToKomentarz1$FormData$Any.PATH, 'post');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<KomentarzResponse>;
    })
  );
}

addOdpowiedzToKomentarz1$FormData$Any.PATH = '/komentarze/odpowiedzi';
