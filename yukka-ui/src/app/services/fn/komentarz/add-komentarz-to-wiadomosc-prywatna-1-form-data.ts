/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { KomentarzRequest } from '../../models/komentarz-request';
import { KomentarzResponse } from '../../models/komentarz-response';

export interface AddKomentarzToWiadomoscPrywatna1$FormData$Params {
  'other-uzyt-nazwa': string;
      body: {
'request': KomentarzRequest;
'file': Blob;
}
}

export function addKomentarzToWiadomoscPrywatna1$FormData(http: HttpClient, rootUrl: string, params: AddKomentarzToWiadomoscPrywatna1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<KomentarzResponse>> {
  const rb = new RequestBuilder(rootUrl, addKomentarzToWiadomoscPrywatna1$FormData.PATH, 'post');
  if (params) {
    rb.path('other-uzyt-nazwa', params['other-uzyt-nazwa'], {});
    rb.body(params.body, 'multipart/form-data');
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

addKomentarzToWiadomoscPrywatna1$FormData.PATH = '/komentarze/wiadomosciPrywatne/{other-uzyt-nazwa}';
