/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface UpdateRoslinaObraz$Params {
  uuid: string;
      body?: {
'file'?: Blob;
}
}

export function updateRoslinaObraz(http: HttpClient, rootUrl: string, params: UpdateRoslinaObraz$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, updateRoslinaObraz.PATH, 'put');
  if (params) {
    rb.path('uuid', params.uuid, {});
    rb.body(params.body, 'multipart/form-data');
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

updateRoslinaObraz.PATH = '/rosliny/{uuid}/obraz';
