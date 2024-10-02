/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UzytkownikRoslinaRequest } from '../../models/uzytkownik-roslina-request';

export interface SaveRoslina1$FormData$Params {
      body: {
'request'?: UzytkownikRoslinaRequest;
'file': Blob;
}
}

export function saveRoslina1$FormData(http: HttpClient, rootUrl: string, params: SaveRoslina1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, saveRoslina1$FormData.PATH, 'post');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<string>;
    })
  );
}

saveRoslina1$FormData.PATH = '/uzytkownikRosliny';
