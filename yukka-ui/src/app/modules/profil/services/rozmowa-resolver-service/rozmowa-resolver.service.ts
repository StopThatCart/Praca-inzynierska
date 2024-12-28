import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { RozmowaPrywatnaResponse } from '../../../../services/models';
import { RozmowaPrywatnaService } from '../../../../services/services';
import { catchError, Observable } from 'rxjs';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Injectable({
  providedIn: 'root'
})
export class RozmowaResolverService implements Resolve<RozmowaPrywatnaResponse> {
  constructor(
    private rozService : RozmowaPrywatnaService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RozmowaPrywatnaResponse> {
    const nazwa = route.paramMap.get('uzytkownik-nazwa');
    return this.rozService.getRozmowaPrywatna({ 'uzytkownik-nazwa': nazwa as string } ).pipe(
      catchError((error) => {
              this.errorHandlingService.handleResolverErrors(error, this.router);
              throw error;
            })
    );
  }
}
