/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { KomentarzRequest } from '../../models/komentarz-request';
import { KomentarzResponse } from '../../models/komentarz-response';

export interface AddOdpowiedzToKomentarz1$Json$Params {
      body: KomentarzRequest
}

export function addOdpowiedzToKomentarz1$Json(http: HttpClient, rootUrl: string, params: AddOdpowiedzToKomentarz1$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
  const rb = new RequestBuilder(rootUrl, addOdpowiedzToKomentarz1$Json.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
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

addOdpowiedzToKomentarz1$Json.PATH = '/komentarze/odpowiedzi';
