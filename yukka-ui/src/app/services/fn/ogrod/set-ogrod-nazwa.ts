/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface SetOgrodNazwa$Params {
  nazwa: string;
}

export function setOgrodNazwa(http: HttpClient, rootUrl: string, params: SetOgrodNazwa$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, setOgrodNazwa.PATH, 'patch');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<string>;
    })
  );
}

setOgrodNazwa.PATH = '/ogrody/{nazwa}';
