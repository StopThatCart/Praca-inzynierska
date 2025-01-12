/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ZasadzonaRoslinaResponse } from '../../models/zasadzona-roslina-response';

export interface GetRoslinaInDzialkaByUuid$Params {
  numer: number;
  uuid: string;
}

export function getRoslinaInDzialkaByUuid(http: HttpClient, rootUrl: string, params: GetRoslinaInDzialkaByUuid$Params, context?: HttpContext): Observable<StrictHttpResponse<ZasadzonaRoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, getRoslinaInDzialkaByUuid.PATH, 'get');
  if (params) {
    rb.path('numer', params.numer, {});
    rb.path('uuid', params.uuid, {});
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

getRoslinaInDzialkaByUuid.PATH = '/dzialki/{numer}/{uuid}';
