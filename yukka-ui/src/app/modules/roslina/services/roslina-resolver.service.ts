import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { RoslinaService } from '../../../services/services';
import { RoslinaResponse } from '../../../services/models/roslina-response';


@Injectable({
  providedIn: 'root'
})
export class RoslinaResolverService implements Resolve<RoslinaResponse> {
  constructor(private roslinaService: RoslinaService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RoslinaResponse> {
    const nazwaLacinska = route.paramMap.get('nazwaLacinska');
    console.log("RoslinaResolverService:resolve: nazwaLacinska: ", nazwaLacinska);
    return this.roslinaService.findByNazwaLacinska({ 'nazwa-lacinska': nazwaLacinska as string } );
  }
}
