/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseUzytkownikRoslinaResponse } from '../../models/page-response-uzytkownik-roslina-response';

export interface FindAllRosliny$Params {
  page?: number;
  size?: number;
}

export function findAllRosliny(http: HttpClient, rootUrl: string, params?: FindAllRosliny$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseUzytkownikRoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllRosliny.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseUzytkownikRoslinaResponse>;
    })
  );
}

findAllRosliny.PATH = '/rest/neo4j/uzytkownicy/rosliny';
