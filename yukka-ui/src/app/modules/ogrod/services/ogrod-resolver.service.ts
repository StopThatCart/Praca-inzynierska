import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { DzialkaService } from '../../../services/services';
import { catchError, Observable } from 'rxjs';
import { DzialkaResponse, OgrodResponse } from '../../../services/models';
import { OgrodService } from '../../../services/services/ogrod.service';
import { ErrorHandlingService } from '../../../services/error-handler/error-handling.service';

@Injectable({
  providedIn: 'root'
})
export class OgrodResolverService implements Resolve<OgrodResponse> {

  constructor(
    private ogrodService: OgrodService, 
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<OgrodResponse> {
    const nazwa = route.paramMap.get('uzytkownik-nazwa');
    //return this.ogrodService.getDzialki({ 'uzytkownik-nazwa': nazwa as string } );
    return this.ogrodService.getDzialki({ 'uzytkownik-nazwa': nazwa as string } ).pipe(
      catchError((error) => {
        this.errorHandlingService.handleResolverErrors(error, this.router);
        throw error;
      })
    );
  }
}
