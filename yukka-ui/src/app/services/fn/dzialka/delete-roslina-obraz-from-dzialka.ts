/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BaseDzialkaRequest } from '../../models/base-dzialka-request';

export interface DeleteRoslinaObrazFromDzialka$Params {
      body: BaseDzialkaRequest
}

export function deleteRoslinaObrazFromDzialka(http: HttpClient, rootUrl: string, params: DeleteRoslinaObrazFromDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, deleteRoslinaObrazFromDzialka.PATH, 'delete');
  if (params) {
    rb.body(params.body, 'application/json');
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

deleteRoslinaObrazFromDzialka.PATH = '/dzialki/rosliny/obraz';
