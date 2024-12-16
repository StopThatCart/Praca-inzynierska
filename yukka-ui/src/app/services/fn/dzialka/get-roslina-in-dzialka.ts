/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ZasadzonaRoslinaResponse } from '../../models/zasadzona-roslina-response';

export interface GetRoslinaInDzialka$Params {
  numer: number;
  x: number;
  y: number;
}

export function getRoslinaInDzialka(http: HttpClient, rootUrl: string, params: GetRoslinaInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<ZasadzonaRoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, getRoslinaInDzialka.PATH, 'get');
  if (params) {
    rb.path('numer', params.numer, {});
    rb.path('x', params.x, {});
    rb.path('y', params.y, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ZasadzonaRoslinaResponse>;
    })
  );
}

getRoslinaInDzialka.PATH = '/dzialki/{numer}/{x}/{y}';
