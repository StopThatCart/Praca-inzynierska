import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { PostResponse } from '../../../services/models';
import { PostService } from '../../../services/services/post.service';
import { catchError, Observable } from 'rxjs';
import { ErrorHandlingService } from '../../../services/error-handler/error-handling.service';

@Injectable({
  providedIn: 'root'
})
export class PostResolverService implements Resolve<PostResponse> {
  constructor(
    private postService : PostService, 
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PostResponse> {
    const uuid = route.paramMap.get('uuid');
    return this.postService.findPostByUuidCheck({ uuid: uuid as string } ).pipe(
      catchError((error) => {
        this.errorHandlingService.handleResolverErrors(error, this.router);
        throw error;
      })
    );
  }
}
