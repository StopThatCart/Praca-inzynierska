/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UzytkownikRoslinaRequest } from '../../models/uzytkownik-roslina-request';
import { WlasciwoscKatalogResponse } from '../../models/wlasciwosc-katalog-response';

export interface GetUzytkownikWlasciwosciCountFromQuery$Params {
  'uzytkownik-nazwa'?: string;
      body?: UzytkownikRoslinaRequest
}

export function getUzytkownikWlasciwosciCountFromQuery(http: HttpClient, rootUrl: string, params?: GetUzytkownikWlasciwosciCountFromQuery$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<WlasciwoscKatalogResponse>>> {
  const rb = new RequestBuilder(rootUrl, getUzytkownikWlasciwosciCountFromQuery.PATH, 'post');
  if (params) {
    rb.query('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<WlasciwoscKatalogResponse>>;
    })
  );
}

getUzytkownikWlasciwosciCountFromQuery.PATH = '/uzytkownikRosliny/wlasciwosciQuery';
