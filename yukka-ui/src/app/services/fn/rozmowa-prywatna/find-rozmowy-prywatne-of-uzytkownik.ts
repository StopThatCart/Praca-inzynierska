/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseRozmowaPrywatnaResponse } from '../../models/page-response-rozmowa-prywatna-response';

export interface FindRozmowyPrywatneOfUzytkownik$Params {
  page?: number;
  size?: number;
}

export function findRozmowyPrywatneOfUzytkownik(http: HttpClient, rootUrl: string, params?: FindRozmowyPrywatneOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRozmowaPrywatnaResponse>> {
  const rb = new RequestBuilder(rootUrl, findRozmowyPrywatneOfUzytkownik.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseRozmowaPrywatnaResponse>;
    })
  );
}

findRozmowyPrywatneOfUzytkownik.PATH = '/rest/neo4j/rozmowy';
