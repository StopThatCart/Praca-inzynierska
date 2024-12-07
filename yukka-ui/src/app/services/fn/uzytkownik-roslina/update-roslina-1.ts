/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoslinaResponse } from '../../models/roslina-response';
import { UzytkownikRoslinaRequest } from '../../models/uzytkownik-roslina-request';

export interface UpdateRoslina1$Params {
      body: UzytkownikRoslinaRequest
}

export function updateRoslina1(http: HttpClient, rootUrl: string, params: UpdateRoslina1$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, updateRoslina1.PATH, 'patch');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RoslinaResponse>;
    })
  );
}

updateRoslina1.PATH = '/uzytkownikRosliny';
