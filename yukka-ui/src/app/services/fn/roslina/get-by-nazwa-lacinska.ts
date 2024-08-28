/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Roslina } from '../../models/roslina';

export interface GetByNazwaLacinska$Params {
  'nazwa-lacinska': string;
}

export function getByNazwaLacinska(http: HttpClient, rootUrl: string, params: GetByNazwaLacinska$Params, context?: HttpContext): Observable<StrictHttpResponse<Roslina>> {
  const rb = new RequestBuilder(rootUrl, getByNazwaLacinska.PATH, 'get');
  if (params) {
    rb.path('nazwa-lacinska', params['nazwa-lacinska'], {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Roslina>;
    })
  );
}

getByNazwaLacinska.PATH = '/rest/neo4j/rosliny/{nazwa-lacinska}';
