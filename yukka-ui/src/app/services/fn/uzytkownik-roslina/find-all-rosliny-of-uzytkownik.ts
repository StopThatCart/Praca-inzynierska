/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseRoslinaResponse } from '../../models/page-response-roslina-response';
import { UzytkownikRoslinaRequest } from '../../models/uzytkownik-roslina-request';

export interface FindAllRoslinyOfUzytkownik$Params {
  page?: number;
  size?: number;
  'uzytkownik-nazwa'?: string;
      body: UzytkownikRoslinaRequest
}

export function findAllRoslinyOfUzytkownik(http: HttpClient, rootUrl: string, params: FindAllRoslinyOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseRoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllRoslinyOfUzytkownik.PATH, 'post');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
    rb.query('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseRoslinaResponse>;
    })
  );
}

findAllRoslinyOfUzytkownik.PATH = '/uzytkownikRosliny/szukaj';
