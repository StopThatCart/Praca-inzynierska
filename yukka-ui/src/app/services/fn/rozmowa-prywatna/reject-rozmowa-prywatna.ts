/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface RejectRozmowaPrywatna$Params {
  'uzytkownik-nazwa': string;
}

export function rejectRozmowaPrywatna(http: HttpClient, rootUrl: string, params: RejectRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, rejectRozmowaPrywatna.PATH, 'put');
  if (params) {
    rb.path('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<{
      }>;
    })
  );
}

rejectRozmowaPrywatna.PATH = '/rozmowy/{uzytkownik-nazwa}/reject';
