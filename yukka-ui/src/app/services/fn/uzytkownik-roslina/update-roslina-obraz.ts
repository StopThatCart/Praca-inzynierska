/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface UpdateRoslinaObraz$Params {
  roslinaId: string;
      body?: {
'file': Blob;
}
}

export function updateRoslinaObraz(http: HttpClient, rootUrl: string, params: UpdateRoslinaObraz$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, updateRoslinaObraz.PATH, 'post');
  if (params) {
    rb.path('roslinaId', params.roslinaId, {});
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<{
      }>;
    })
  );
}

updateRoslinaObraz.PATH = '/rest/neo4j/uzytkownicy/rosliny/{roslinaId}';
