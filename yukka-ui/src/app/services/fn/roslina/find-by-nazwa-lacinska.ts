/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoslinaResponse } from '../../models/roslina-response';

export interface FindByNazwaLacinska$Params {
  'nazwa-lacinska': string;
}

export function findByNazwaLacinska(http: HttpClient, rootUrl: string, params: FindByNazwaLacinska$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, findByNazwaLacinska.PATH, 'get');
  if (params) {
    rb.path('nazwa-lacinska', params['nazwa-lacinska'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RoslinaResponse>;
    })
  );
}

findByNazwaLacinska.PATH = '/rest/neo4j/rosliny/{nazwa-lacinska}';
