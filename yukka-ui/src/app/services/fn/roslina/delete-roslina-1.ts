/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface DeleteRoslina1$Params {
  'nazwa-lacinska': string;
}

export function deleteRoslina1(http: HttpClient, rootUrl: string, params: DeleteRoslina1$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, deleteRoslina1.PATH, 'delete');
  if (params) {
    rb.path('nazwa-lacinska', params['nazwa-lacinska'], {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<string>;
    })
  );
}

deleteRoslina1.PATH = '/rosliny/{nazwa-lacinska}';
