/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SpecjalnePowiadomienieRequest } from '../../models/specjalne-powiadomienie-request';

export interface SendSpecjalnePowiadomienie$Params {
      body: SpecjalnePowiadomienieRequest
}

export function sendSpecjalnePowiadomienie(http: HttpClient, rootUrl: string, params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, sendSpecjalnePowiadomienie.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<{
      }>;
    })
  );
}

sendSpecjalnePowiadomienie.PATH = '/powiadomienia/pracownik';
