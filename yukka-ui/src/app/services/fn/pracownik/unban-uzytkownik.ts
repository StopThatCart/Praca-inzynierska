/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface UnbanUzytkownik$Params {
  'uzytkownik-nazwa': string;
}

export function unbanUzytkownik(http: HttpClient, rootUrl: string, params: UnbanUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
  const rb = new RequestBuilder(rootUrl, unbanUzytkownik.PATH, 'patch');
  if (params) {
    rb.path('uzytkownik-nazwa', params['uzytkownik-nazwa'], {});
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

unbanUzytkownik.PATH = '/pracownicy/unban/{uzytkownik-nazwa}';
