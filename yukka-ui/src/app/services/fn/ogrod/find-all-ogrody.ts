/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseOgrodResponse } from '../../models/page-response-ogrod-response';

export interface FindAllOgrody$Params {
  page?: number;
  size?: number;
  szukaj?: string;
}

export function findAllOgrody(http: HttpClient, rootUrl: string, params?: FindAllOgrody$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseOgrodResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllOgrody.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
    rb.query('szukaj', params.szukaj, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseOgrodResponse>;
    })
  );
}

findAllOgrody.PATH = '/ogrody';
