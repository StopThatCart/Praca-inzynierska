import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { catchError, Observable, of } from 'rxjs';
import { RoslinaService } from '../../../services/services';
import { RoslinaResponse } from '../../../services/models/roslina-response';
import { ErrorHandlingService } from '../../../services/error-handler/error-handling.service';


@Injectable({
  providedIn: 'root'
})
export class RoslinaResolverService implements Resolve<RoslinaResponse> {
  constructor(
    private roslinaService: RoslinaService, 
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RoslinaResponse> {
    const roslinaId = route.paramMap.get('roslina-id');
    return this.roslinaService.findByRoslinaId({ 'roslina-id': roslinaId as string }).pipe(
      catchError((error) => {
        this.errorHandlingService.handleResolverErrors(error, this.router);
        throw error;
      })
    );

  }
}
