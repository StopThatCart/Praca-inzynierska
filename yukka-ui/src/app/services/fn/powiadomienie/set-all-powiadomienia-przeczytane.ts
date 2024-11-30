/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface SetAllPowiadomieniaPrzeczytane$Params {
}

export function setAllPowiadomieniaPrzeczytane(http: HttpClient, rootUrl: string, params?: SetAllPowiadomieniaPrzeczytane$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, setAllPowiadomieniaPrzeczytane.PATH, 'patch');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<{
      }>;
    })
  );
}

setAllPowiadomieniaPrzeczytane.PATH = '/powiadomienia/przeczytane';
