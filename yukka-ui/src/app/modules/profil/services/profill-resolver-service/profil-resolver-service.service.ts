import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { PostResponse, UzytkownikResponse } from '../../../../services/models';
import { PostService } from '../../../../services/services/post.service';
import { catchError, Observable } from 'rxjs';
import { UzytkownikService } from '../../../../services/services';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Injectable({
  providedIn: 'root'
})
export class ProfilResolverService implements Resolve<UzytkownikResponse> {
  constructor(
    private uzytService : UzytkownikService, 
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PostResponse> {
    const nazwa = route.paramMap.get('nazwa');
    //return this.uzytService.findByNazwa({ 'nazwa': nazwa as string } );
    return this.uzytService.findByNazwa({ 'nazwa': nazwa as string } ).pipe(
      catchError((error) => {
        this.errorHandlingService.handleResolverErrors(error, this.router);
        throw error;
      })
    );
  }
}
