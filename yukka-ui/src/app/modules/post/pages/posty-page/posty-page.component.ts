import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PostResponse } from '../../../../services/models/post-response';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../../../../services/services';
import { PostCardComponent } from "../../components/post-card/post-card.component";

@Component({
  selector: 'app-posty-page',
  standalone: true,
  imports: [CommonModule, PostCardComponent],
  templateUrl: './posty-page.component.html',
  styleUrl: './posty-page.component.css'
})
export class PostyPageComponent implements OnInit {
  post : PostResponse | null = null;

  private _postObraz: string | undefined;

  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const postId = params['postId'];
      if (postId) {
        this.getPostByPostId(postId);
        this.route.snapshot.data['postId'] = postId;
      }
    });

  }

  getPostByPostId(postId: string): void {
    this.postService.findPostById({ 'post-id': postId }).subscribe({
      next: (post) => {
        this.post = post;
        this.errorMessage = null;
      },
      error: (err) => {
        this.post = null;
        this.errorMessage = 'Nie znaleziono posta o podanym ID.';
      }
    });
  }

  getPostObraz(): string | undefined {
    if(this.post && this.post.obraz) {
      return 'data:image/jpeg;base64,' + this.post.obraz;
    }
    return this._postObraz;
  }

}
