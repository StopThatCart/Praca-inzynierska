import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { PostResponse, UzytkownikResponse } from '../../../../services/models';
import { PostService } from '../../../../services/services/post.service';
import { catchError, map, Observable } from 'rxjs';
import { UzytkownikService } from '../../../../services/services';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { TokenService } from '../../../../services/token/token.service';

@Injectable({
  providedIn: 'root'
})
export class ProfilResolverService implements Resolve<UzytkownikResponse> {
  constructor(
    private uzytService : UzytkownikService, 
    private router: Router,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UzytkownikResponse> {
    const nazwa = route.paramMap.get('nazwa');
    const uzyt: Observable<UzytkownikResponse> = this.uzytService.findByNazwa({ 'nazwa': nazwa as string } ).pipe(
      map((response: UzytkownikResponse) => {
        if (!response.aktywowany && !this.tokenService.isAdmin()) {
          throw new Error('Użytkownik nie jest aktywowany');
        }
        return response;
      }),
      catchError((error) => {
        this.errorHandlingService.handleResolverErrors(error, this.router);
        throw error;
      })
    );
    
    return uzyt;
  }
}
