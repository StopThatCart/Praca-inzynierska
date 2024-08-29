/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PostResponse } from '../../models/post-response';

export interface FindPostById$Params {
  'post-id': string;
}

export function findPostById(http: HttpClient, rootUrl: string, params: FindPostById$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
  const rb = new RequestBuilder(rootUrl, findPostById.PATH, 'get');
  if (params) {
    rb.path('post-id', params['post-id'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PostResponse>;
    })
  );
}

findPostById.PATH = '/rest/neo4j/posty/{post-id}';
