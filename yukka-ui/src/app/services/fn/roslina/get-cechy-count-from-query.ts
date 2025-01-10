/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { CechaKatalogResponse } from '../../models/cecha-katalog-response';
import { RoslinaRequest } from '../../models/roslina-request';

export interface GetCechyCountFromQuery$Params {
      body?: RoslinaRequest
}

export function getCechyCountFromQuery(http: HttpClient, rootUrl: string, params?: GetCechyCountFromQuery$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<CechaKatalogResponse>>> {
  const rb = new RequestBuilder(rootUrl, getCechyCountFromQuery.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<CechaKatalogResponse>>;
    })
  );
}

getCechyCountFromQuery.PATH = '/rosliny/cechyQuery';
