import { Injectable } from '@angular/core';
import { DzialkaResponse } from '../../../../services/models';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, RouterStateSnapshot } from '@angular/router';
import { DzialkaService } from '../../../../services/services/dzialka.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DzialkaResolverService implements Resolve<DzialkaResponse> {

  constructor(private dzialkaService : DzialkaService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<DzialkaResponse> {
    const nazwa = route.paramMap.get('uzytkownik-nazwa');
    const numer = Number(route.paramMap.get('numer'));
    return this.dzialkaService.getDzialkaOfUzytkownikByNumer({ numer: numer as number, 'uzytkownik-nazwa': nazwa as string } );
  }
}
