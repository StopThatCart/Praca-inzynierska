/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RozmowaPrywatnaResponse } from '../../models/rozmowa-prywatna-response';

export interface GetRozmowaPrywatna$Params {
}

export function getRozmowaPrywatna(http: HttpClient, rootUrl: string, params?: GetRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatnaResponse>> {
  const rb = new RequestBuilder(rootUrl, getRozmowaPrywatna.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RozmowaPrywatnaResponse>;
    })
  );
}

getRozmowaPrywatna.PATH = '/rozmowy/{odbiorca-uzyt-id}';
