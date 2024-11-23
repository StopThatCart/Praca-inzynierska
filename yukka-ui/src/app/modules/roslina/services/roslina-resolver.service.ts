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
    const roslinaId = route.paramMap.get('roslina-id');
    console.log("RoslinaResolverService:resolve: roslinaId: ", roslinaId);
    return this.roslinaService.findByRoslinaId({ 'roslina-id': roslinaId as string } );
    //return this.roslinaService.findByNazwaLacinska({ 'nazwa-lacinska': nazwaLacinska as string } );
  }
}
