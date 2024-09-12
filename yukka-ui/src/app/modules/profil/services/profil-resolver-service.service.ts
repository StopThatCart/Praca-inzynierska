import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, RouterStateSnapshot } from '@angular/router';
import { PostResponse, UzytkownikResponse } from '../../../services/models';
import { PostService } from '../../../services/services/post.service';
import { Observable } from 'rxjs';
import { UzytkownikService } from '../../../services/services';

@Injectable({
  providedIn: 'root'
})
export class ProfilResolverService implements Resolve<UzytkownikResponse> {
  constructor(private uzytService : UzytkownikService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PostResponse> {
    const nazwa = route.paramMap.get('nazwa');
    return this.uzytService.findByNazwa({ 'nazwa': nazwa as string } );
  }
}
