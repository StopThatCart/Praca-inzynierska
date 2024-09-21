/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoslinaRequest } from '../../models/roslina-request';
import { RoslinaResponse } from '../../models/roslina-response';

export interface SaveRoslina2$FormData$Params {
      body: {
'request': RoslinaRequest;
'file': Blob;
}
}

export function saveRoslina2$FormData(http: HttpClient, rootUrl: string, params: SaveRoslina2$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, saveRoslina2$FormData.PATH, 'post');
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

saveRoslina2$FormData.PATH = '/rosliny';
