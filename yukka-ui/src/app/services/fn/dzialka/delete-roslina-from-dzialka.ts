/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DzialkaRoslinaRequest } from '../../models/dzialka-roslina-request';

export interface DeleteRoslinaFromDzialka$Params {
      body: DzialkaRoslinaRequest
}

export function deleteRoslinaFromDzialka(http: HttpClient, rootUrl: string, params: DeleteRoslinaFromDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, deleteRoslinaFromDzialka.PATH, 'delete');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<string>;
    })
  );
}

deleteRoslinaFromDzialka.PATH = '/rest/neo4j/dzialki/rosliny';
