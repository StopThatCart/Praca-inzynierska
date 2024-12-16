/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ZasadzonaRoslinaResponse } from '../../models/zasadzona-roslina-response';

export interface GetRoslinaInDzialkaByRoslinaId$Params {
  numer: number;
  'roslina-id': string;
}

export function getRoslinaInDzialkaByRoslinaId(http: HttpClient, rootUrl: string, params: GetRoslinaInDzialkaByRoslinaId$Params, context?: HttpContext): Observable<StrictHttpResponse<ZasadzonaRoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, getRoslinaInDzialkaByRoslinaId.PATH, 'get');
  if (params) {
    rb.path('numer', params.numer, {});
    rb.path('roslina-id', params['roslina-id'], {});
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

getRoslinaInDzialkaByRoslinaId.PATH = '/dzialki/{numer}/{roslina-id}';
