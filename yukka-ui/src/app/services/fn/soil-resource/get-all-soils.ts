/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Gleba } from '../../models/gleba';

export interface GetAllSoils$Params {
}

export function getAllSoils(http: HttpClient, rootUrl: string, params?: GetAllSoils$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Gleba>>> {
  const rb = new RequestBuilder(rootUrl, getAllSoils.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<Gleba>>;
    })
  );
}

getAllSoils.PATH = '/rest/neo4j/gleba';
