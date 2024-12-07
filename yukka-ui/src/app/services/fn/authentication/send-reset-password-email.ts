/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface SendResetPasswordEmail$Params {
  email: string;
}

export function sendResetPasswordEmail(http: HttpClient, rootUrl: string, params: SendResetPasswordEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, sendResetPasswordEmail.PATH, 'post');
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

sendResetPasswordEmail.PATH = '/api/auth/zmiana-hasla-email/{email}';
