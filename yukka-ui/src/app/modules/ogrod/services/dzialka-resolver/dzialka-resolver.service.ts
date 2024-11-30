import { Injectable } from '@angular/core';
import { DzialkaResponse } from '../../../../services/models';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { DzialkaService } from '../../../../services/services/dzialka.service';
import { catchError, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DzialkaResolverService implements Resolve<DzialkaResponse> {

  constructor(private dzialkaService : DzialkaService, private router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<DzialkaResponse> {
    const nazwa = route.paramMap.get('uzytkownik-nazwa');
    const numer = Number(route.paramMap.get('numer'));
    //return this.dzialkaService.getDzialkaOfUzytkownikByNumer({ numer: numer as number, 'uzytkownik-nazwa': nazwa as string } );
    return this.dzialkaService.getDzialkaOfUzytkownikByNumer({ numer: numer as number, 'uzytkownik-nazwa': nazwa as string } ).pipe(
      catchError((error) => {
        this.router.navigate(['/404']);
        throw error;
      })
    );
  }
}
