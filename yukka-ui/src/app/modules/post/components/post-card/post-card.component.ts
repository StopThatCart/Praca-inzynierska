import { Component, Input } from '@angular/core';
import { PostResponse } from '../../../../services/models';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { addOcenaToPost } from '../../../../services/fn/post/add-ocena-to-post';
import { PostService } from '../../../../services/services';
import { OcenaRequest } from '../../../../services/models/ocena-request';
import { remove } from '../../../../services/fn/uzytkownik/remove';
import { removeOcenaFromPost } from '../../../../services/fn/post/remove-ocena-from-post';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.css'
})
export class PostCardComponent {
  @Input()  post:PostResponse = {};
  private _postObraz: string | undefined;
  private _postAvatar: string | undefined;

  // TODO: Time ago.

  constructor(private router: Router,
              private postService: PostService
  ) {}

  getPost(): PostResponse {
    console.log('getPost');
    console.log(this.post);
    return this.post;
  }

  setPost(post: PostResponse) {
    this.post = post;
  }

  getPostObraz(): string | undefined {
    if(this.post.obraz) {
      return 'data:image/jpeg;base64,' + this.post.obraz;
    }
    return this._postObraz;
  }

  getPostAvatar(): string | undefined {
    if(this.post.avatar) {
      return 'data:image/jpeg;base64,' + this.post.avatar;
    }
    return this._postAvatar;
  }

  goToPost(postId: string | undefined) {
    if (postId) {
      this.router.navigate(['/posty', postId]);
    }
  }

  addOcenaToPost(postId: string | undefined, ocena: boolean) {
    if (postId) {


      let ocenaRequest: OcenaRequest = { lubi: ocena, ocenialnyId: postId };
      this.postService.addOcenaToPost({ body: ocenaRequest }).subscribe({
        next: (post) => {
          this.post = post;
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === 403) {
            this.router.navigate(['/login']);
          } else {
            console.error('Wystąpił błąd:', error);
          }
        }
      });
    }
  }

  removeOcenaFromPost(postId: string | undefined) {
    if (postId) {
      let ocenaRequest: OcenaRequest = { lubi: true, ocenialnyId: postId };
      this.postService.removeOcenaFromPost({ body: ocenaRequest }).subscribe({
        next: () => {
          this.postService.findPostById({ 'post-id': postId }).subscribe({
            next: (post) => {
              this.post = post;
            },
            error: (err) => {
              console.log(err);
            }
          });
        },
        error: (err) => {
          console.log(err);
        }
      });

    }
  }

}
