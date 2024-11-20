/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface SetBanUzytkownik$Params {
  nazwa: string;
  ban: boolean;
}

export function setBanUzytkownik(http: HttpClient, rootUrl: string, params: SetBanUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
  const rb = new RequestBuilder(rootUrl, setBanUzytkownik.PATH, 'patch');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
    rb.query('ban', params.ban, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: String((r as HttpResponse<any>).body) === 'true' }) as StrictHttpResponse<boolean>;
    })
  );
}

setBanUzytkownik.PATH = '/uzytkownicy/pracownik/ban/{nazwa}';
