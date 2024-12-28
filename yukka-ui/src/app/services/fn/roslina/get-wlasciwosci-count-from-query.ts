/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoslinaRequest } from '../../models/roslina-request';
import { WlasciwoscKatalogResponse } from '../../models/wlasciwosc-katalog-response';

export interface GetWlasciwosciCountFromQuery$Params {
      body?: RoslinaRequest
}

export function getWlasciwosciCountFromQuery(http: HttpClient, rootUrl: string, params?: GetWlasciwosciCountFromQuery$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<WlasciwoscKatalogResponse>>> {
  const rb = new RequestBuilder(rootUrl, getWlasciwosciCountFromQuery.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<WlasciwoscKatalogResponse>>;
    })
  );
}

getWlasciwosciCountFromQuery.PATH = '/rosliny/wlasciwosciQuery';
