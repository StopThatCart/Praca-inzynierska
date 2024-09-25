import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { DzialkaService } from '../../../services/services';
import { Observable } from 'rxjs';
import { DzialkaResponse, OgrodResponse } from '../../../services/models';
import { OgrodService } from '../../../services/services/ogrod.service';

@Injectable({
  providedIn: 'root'
})
export class OgrodResolverService implements Resolve<OgrodResponse> {

  constructor(private ogrodService: OgrodService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<OgrodResponse> {
    const nazwa = route.paramMap.get('uzytkownik-nazwa');
    return this.ogrodService.getDzialki({ 'uzytkownik-nazwa': nazwa as string } );
  }
}
