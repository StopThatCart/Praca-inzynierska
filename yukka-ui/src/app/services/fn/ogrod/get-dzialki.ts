/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { OgrodResponse } from '../../models/ogrod-response';

export interface GetDzialki$Params {
  'uzytkownik-nazwa': string;
}

export function getDzialki(http: HttpClient, rootUrl: string, params: GetDzialki$Params, context?: HttpContext): Observable<StrictHttpResponse<OgrodResponse>> {
  const rb = new RequestBuilder(rootUrl, getDzialki.PATH, 'get');
  if (params) {
    rb.path('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<OgrodResponse>;
    })
  );
}

getDzialki.PATH = '/ogrody/{uzytkownik-nazwa}';
