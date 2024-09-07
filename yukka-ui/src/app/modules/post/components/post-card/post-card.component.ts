import { Component, Input } from '@angular/core';
import { PostResponse } from '../../../../services/models';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

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

  constructor(private router: Router) {}

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

}
