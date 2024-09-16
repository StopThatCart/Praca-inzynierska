/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RozmowaPrywatnaResponse } from '../../models/rozmowa-prywatna-response';

export interface GetRozmowaPrywatnaById$Params {
  id: number;
}

export function getRozmowaPrywatnaById(http: HttpClient, rootUrl: string, params: GetRozmowaPrywatnaById$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatnaResponse>> {
  const rb = new RequestBuilder(rootUrl, getRozmowaPrywatnaById.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
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

getRozmowaPrywatnaById.PATH = '/rozmowy/id/{id}';
