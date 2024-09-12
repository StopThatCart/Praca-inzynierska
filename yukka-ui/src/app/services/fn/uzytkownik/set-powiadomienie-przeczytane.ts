/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Powiadomienie } from '../../models/powiadomienie';

export interface SetPowiadomieniePrzeczytane$Params {
  nazwa: string;
  id: number;
}

export function setPowiadomieniePrzeczytane(http: HttpClient, rootUrl: string, params: SetPowiadomieniePrzeczytane$Params, context?: HttpContext): Observable<StrictHttpResponse<Powiadomienie>> {
  const rb = new RequestBuilder(rootUrl, setPowiadomieniePrzeczytane.PATH, 'put');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Powiadomienie>;
    })
  );
}

setPowiadomieniePrzeczytane.PATH = '/uzytkownicy/{nazwa}/powiadomienie/{id}/przeczytane';
