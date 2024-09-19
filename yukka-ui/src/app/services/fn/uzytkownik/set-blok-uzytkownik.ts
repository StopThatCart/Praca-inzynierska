/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface SetBlokUzytkownik$Params {
  nazwa: string;
  blok: boolean;
}

export function setBlokUzytkownik(http: HttpClient, rootUrl: string, params: SetBlokUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
  const rb = new RequestBuilder(rootUrl, setBlokUzytkownik.PATH, 'patch');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
    rb.path('blok', params.blok, {});
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

setBlokUzytkownik.PATH = '/uzytkownicy/blok/{nazwa}/{blok}';
