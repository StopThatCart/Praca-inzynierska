/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RozmowaPrywatna } from '../../models/rozmowa-prywatna';

export interface InviteToRozmowaPrywatna$Params {
  nazwa: string;
}

export function inviteToRozmowaPrywatna(http: HttpClient, rootUrl: string, params: InviteToRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatna>> {
  const rb = new RequestBuilder(rootUrl, inviteToRozmowaPrywatna.PATH, 'post');
  if (params) {
    rb.path('nazwa', params.nazwa, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RozmowaPrywatna>;
    })
  );
}

inviteToRozmowaPrywatna.PATH = '/rozmowy/{nazwa}';
