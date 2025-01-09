/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface AcceptRozmowaPrywatna$Params {
  'uzytkownik-nazwa': string;
}

export function acceptRozmowaPrywatna(http: HttpClient, rootUrl: string, params: AcceptRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, acceptRozmowaPrywatna.PATH, 'put');
  if (params) {
    rb.path('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<{
      }>;
    })
  );
}

acceptRozmowaPrywatna.PATH = '/rozmowy/{uzytkownik-nazwa}/accept';
