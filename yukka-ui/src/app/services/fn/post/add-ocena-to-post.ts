/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { OcenaRequest } from '../../models/ocena-request';
import { OcenaResponse } from '../../models/ocena-response';

export interface AddOcenaToPost$Params {
      body: OcenaRequest
}

export function addOcenaToPost(http: HttpClient, rootUrl: string, params: AddOcenaToPost$Params, context?: HttpContext): Observable<StrictHttpResponse<OcenaResponse>> {
  const rb = new RequestBuilder(rootUrl, addOcenaToPost.PATH, 'put');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<OcenaResponse>;
    })
  );
}

addOcenaToPost.PATH = '/posty/oceny';
