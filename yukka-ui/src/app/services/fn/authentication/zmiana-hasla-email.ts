/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface ZmianaHaslaEmail$Params {
  email: string;
}

export function zmianaHaslaEmail(http: HttpClient, rootUrl: string, params: ZmianaHaslaEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, zmianaHaslaEmail.PATH, 'post');
  if (params) {
    rb.path('email', params.email, {});
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

zmianaHaslaEmail.PATH = '/api/auth/zmiana-hasla-email/{email}';
