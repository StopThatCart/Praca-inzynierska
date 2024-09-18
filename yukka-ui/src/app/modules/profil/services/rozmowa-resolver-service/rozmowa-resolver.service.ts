import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { RozmowaPrywatnaResponse } from '../../../../services/models';
import { RozmowaPrywatnaService } from '../../../../services/services';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RozmowaResolverService implements Resolve<RozmowaPrywatnaResponse> {
  constructor(private rozService : RozmowaPrywatnaService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RozmowaPrywatnaResponse> {
    const nazwa = route.paramMap.get('uzytkownikNazwa');
    return this.rozService.getRozmowaPrywatna({ 'uzytkownikNazwa': nazwa as string } );
  }
}
