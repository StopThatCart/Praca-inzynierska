/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DzialkaRoslinaRequest } from '../../models/dzialka-roslina-request';
import { FileResponse } from '../../models/file-response';

export interface UpdateRoslinaObrazInDzialka$Params {
      body?: {
'request': DzialkaRoslinaRequest;
'obraz'?: Blob;
'tekstura'?: Blob;
}
}

export function updateRoslinaObrazInDzialka(http: HttpClient, rootUrl: string, params?: UpdateRoslinaObrazInDzialka$Params, context?: HttpContext): Observable<StrictHttpResponse<FileResponse>> {
  const rb = new RequestBuilder(rootUrl, updateRoslinaObrazInDzialka.PATH, 'patch');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
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

updateRoslinaObrazInDzialka.PATH = '/dzialki/rosliny/obraz';
