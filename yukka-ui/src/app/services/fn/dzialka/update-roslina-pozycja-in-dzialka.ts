/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DzialkaResponse } from '../../models/dzialka-response';
import { MoveRoslinaRequest } from '../../models/move-roslina-request';

export interface UpdateRoslinaPozycjaInDzialka$Params {
      body: MoveRoslinaRequest
}

export function updateRoslinaPozycjaInDzialka(http: HttpClient, rootUrl: string, params: UpdateRoslinaPozycjaInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
  const rb = new RequestBuilder(rootUrl, updateRoslinaPozycjaInDzialka.PATH, 'patch');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<DzialkaResponse>;
    })
  );
}

updateRoslinaPozycjaInDzialka.PATH = '/dzialki/rosliny/pozycja';
