/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseRoslinaResponse } from '../../models/page-response-roslina-response';

export interface FindAllRosliny1$Params {
  page?: number;
  size?: number;
}

export function findAllRosliny1(http: HttpClient, rootUrl: string, params?: FindAllRosliny1$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllRosliny1.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseRoslinaResponse>;
    })
  );
}

findAllRosliny1.PATH = '/rest/neo4j/rosliny';
