/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Roslina } from '../../models/roslina';

export interface FindUzytkownikRoslinaByRoslinaId$Params {
  roslinaId: string;
}

export function findUzytkownikRoslinaByRoslinaId(http: HttpClient, rootUrl: string, params: FindUzytkownikRoslinaByRoslinaId$Params, context?: HttpContext): Observable<StrictHttpResponse<Roslina>> {
  const rb = new RequestBuilder(rootUrl, findUzytkownikRoslinaByRoslinaId.PATH, 'get');
  if (params) {
    rb.path('roslinaId', params.roslinaId, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Roslina>;
    })
  );
}

findUzytkownikRoslinaByRoslinaId.PATH = '/uzytkownikRosliny/{roslinaId}';
