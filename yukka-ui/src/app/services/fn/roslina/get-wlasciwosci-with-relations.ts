/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { WlasciwoscResponse } from '../../models/wlasciwosc-response';

export interface GetWlasciwosciWithRelations$Params {
}

export function getWlasciwosciWithRelations(http: HttpClient, rootUrl: string, params?: GetWlasciwosciWithRelations$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<WlasciwoscResponse>>> {
  const rb = new RequestBuilder(rootUrl, getWlasciwosciWithRelations.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<WlasciwoscResponse>>;
    })
  );
}

getWlasciwosciWithRelations.PATH = '/rest/neo4j/rosliny/wlasciwosci';
