/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Powiadomienie } from '../../models/powiadomienie';
import { PowiadomienieDto } from '../../models/powiadomienie-dto';

export interface SendSpecjalnePowiadomienie$Params {
      body: PowiadomienieDto
}

export function sendSpecjalnePowiadomienie(http: HttpClient, rootUrl: string, params: SendSpecjalnePowiadomienie$Params, context?: HttpContext): Observable<StrictHttpResponse<Powiadomienie>> {
  const rb = new RequestBuilder(rootUrl, sendSpecjalnePowiadomienie.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Powiadomienie>;
    })
  );
}

sendSpecjalnePowiadomienie.PATH = '/rest/neo4j/uzytkownicy/pracownik/powiadomienie';
