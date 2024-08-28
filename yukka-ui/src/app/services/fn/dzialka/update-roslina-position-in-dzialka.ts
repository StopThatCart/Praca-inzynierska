/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DzialkaResponse } from '../../models/dzialka-response';
import { MoveRoslinaRequest } from '../../models/move-roslina-request';

export interface UpdateRoslinaPositionInDzialka$Params {
      body: MoveRoslinaRequest
}

export function updateRoslinaPositionInDzialka(http: HttpClient, rootUrl: string, params: UpdateRoslinaPositionInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
  const rb = new RequestBuilder(rootUrl, updateRoslinaPositionInDzialka.PATH, 'patch');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<DzialkaResponse>;
    })
  );
}

updateRoslinaPositionInDzialka.PATH = '/rest/neo4j/dzialki/rosliny/pozycja';
