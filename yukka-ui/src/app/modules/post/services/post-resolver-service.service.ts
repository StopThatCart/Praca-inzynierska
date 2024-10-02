import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, MaybeAsync, Resolve, RouterStateSnapshot } from '@angular/router';
import { PostResponse } from '../../../services/models';
import { PostService } from '../../../services/services/post.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostResolverService implements Resolve<PostResponse> {
  constructor(private postService : PostService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PostResponse> {
    const postId = route.paramMap.get('postId');
    console.log("PostResolverService:resolve: postId: ", postId);
    return this.postService.findPostById({ 'post-id': postId as string } );
  }
}
