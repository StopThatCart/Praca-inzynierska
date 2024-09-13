/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PowiadomienieDto } from '../../models/powiadomienie-dto';
import { PowiadomienieResponse } from '../../models/powiadomienie-response';

export interface SendSpecjalnePowiadomienieToPracownicy$Params {
      body: PowiadomienieDto
}

export function sendSpecjalnePowiadomienieToPracownicy(http: HttpClient, rootUrl: string, params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<PowiadomienieResponse>> {
  const rb = new RequestBuilder(rootUrl, sendSpecjalnePowiadomienieToPracownicy.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PowiadomienieResponse>;
    })
  );
}

sendSpecjalnePowiadomienieToPracownicy.PATH = '/powiadomienia/admin';
