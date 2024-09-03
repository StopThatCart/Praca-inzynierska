/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseRoslinaResponse } from '../../models/page-response-roslina-response';
import { RoslinaRequest } from '../../models/roslina-request';

export interface FindAllRoslinyWithParameters$Params {
  page?: number;
  size?: number;
      body: RoslinaRequest
}

export function findAllRoslinyWithParameters(http: HttpClient, rootUrl: string, params: FindAllRoslinyWithParameters$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllRoslinyWithParameters.PATH, 'post');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseRoslinaResponse>;
    })
  );
}

findAllRoslinyWithParameters.PATH = '/rest/neo4j/rosliny/szukaj';
