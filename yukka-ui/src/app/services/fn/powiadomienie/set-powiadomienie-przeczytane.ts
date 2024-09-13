/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PowiadomienieResponse } from '../../models/powiadomienie-response';

export interface SetPowiadomieniePrzeczytane$Params {
  id: number;
}

export function setPowiadomieniePrzeczytane(http: HttpClient, rootUrl: string, params: SetPowiadomieniePrzeczytane$Params, context?: HttpContext): Observable<StrictHttpResponse<PowiadomienieResponse>> {
  const rb = new RequestBuilder(rootUrl, setPowiadomieniePrzeczytane.PATH, 'patch');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PowiadomienieResponse>;
    })
  );
}

setPowiadomieniePrzeczytane.PATH = '/powiadomienia/{id}/przeczytane';
