/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DzialkaResponse } from '../../models/dzialka-response';
import { DzialkaRoslinaRequest } from '../../models/dzialka-roslina-request';

export interface UpdateRoslinaWyswietlanieInDzialka$Params {
      body: DzialkaRoslinaRequest
}

export function updateRoslinaWyswietlanieInDzialka(http: HttpClient, rootUrl: string, params: UpdateRoslinaWyswietlanieInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<DzialkaResponse>> {
  const rb = new RequestBuilder(rootUrl, updateRoslinaWyswietlanieInDzialka.PATH, 'patch');
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

updateRoslinaWyswietlanieInDzialka.PATH = '/dzialki/rosliny/wyswietlanie';
