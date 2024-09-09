/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface RemoveKomentarzFromPost$Params {
  'post-id': string;
  'komentarz-id': string;
}

export function removeKomentarzFromPost(http: HttpClient, rootUrl: string, params: RemoveKomentarzFromPost$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, removeKomentarzFromPost.PATH, 'delete');
  if (params) {
    rb.path('post-id', params['post-id'], {});
    rb.path('komentarz-id', params['komentarz-id'], {});
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

removeKomentarzFromPost.PATH = '/komentarze/{komentarz-id}/posty/{post-id}';
