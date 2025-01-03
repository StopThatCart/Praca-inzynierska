/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { FileResponse } from '../../models/file-response';

export interface GetAvatar$Params {
}

export function getAvatar(http: HttpClient, rootUrl: string, params?: GetAvatar$Params, context?: HttpContext): Observable<StrictHttpResponse<FileResponse>> {
  const rb = new RequestBuilder(rootUrl, getAvatar.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<FileResponse>;
    })
  );
}

getAvatar.PATH = '/uzytkownicy/avatar';
