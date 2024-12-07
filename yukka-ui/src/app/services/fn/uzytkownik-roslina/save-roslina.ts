/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoslinaResponse } from '../../models/roslina-response';
import { UzytkownikRoslinaRequest } from '../../models/uzytkownik-roslina-request';

export interface SaveRoslina$Params {
      body?: {
'request': UzytkownikRoslinaRequest;
'file'?: Blob;
}
}

export function saveRoslina(http: HttpClient, rootUrl: string, params?: SaveRoslina$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, saveRoslina.PATH, 'post');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RoslinaResponse>;
    })
  );
}

saveRoslina.PATH = '/uzytkownikRosliny';
