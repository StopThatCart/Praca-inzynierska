/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RozmowaPrywatnaResponse } from '../../models/rozmowa-prywatna-response';

export interface FindRozmowaPrywatnaByNazwa$Params {
  'uzytkownik-nazwa': string;
}

export function findRozmowaPrywatnaByNazwa(http: HttpClient, rootUrl: string, params: FindRozmowaPrywatnaByNazwa$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatnaResponse>> {
  const rb = new RequestBuilder(rootUrl, findRozmowaPrywatnaByNazwa.PATH, 'get');
  if (params) {
    rb.path('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
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

findRozmowaPrywatnaByNazwa.PATH = '/rozmowy/{uzytkownik-nazwa}';
