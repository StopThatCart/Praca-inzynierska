/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BlokResponse } from '../../models/blok-response';

export interface IsBlok$Params {
  nazwa: string;
}

export function isBlok(http: HttpClient, rootUrl: string, params: IsBlok$Params, context?: HttpContext): Observable<StrictHttpResponse<BlokResponse>> {
  const rb = new RequestBuilder(rootUrl, isBlok.PATH, 'get');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<BlokResponse>;
    })
  );
}

isBlok.PATH = '/uzytkownicy/blok/{nazwa}';
