/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Powiadomienie } from '../../models/powiadomienie';
import { PowiadomienieDto } from '../../models/powiadomienie-dto';

export interface SendSpecjalnePowiadomienieToPracownicy$Params {
      body: PowiadomienieDto
}

export function sendSpecjalnePowiadomienieToPracownicy(http: HttpClient, rootUrl: string, params: SendSpecjalnePowiadomienieToPracownicy$Params, context?: HttpContext): Observable<StrictHttpResponse<Powiadomienie>> {
  const rb = new RequestBuilder(rootUrl, sendSpecjalnePowiadomienieToPracownicy.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Powiadomienie>;
    })
  );
}

sendSpecjalnePowiadomienieToPracownicy.PATH = '/rest/neo4j/uzytkownicy/admin/powiadomienie';
