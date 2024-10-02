import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PostResponse } from '../../../../services/models/post-response';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../../../../services/services';
import { PostCardComponent } from "../../components/post-card/post-card.component";
import { KomentarzCardComponent } from '../../components/komentarz-card/komentarz-card.component';
import { AddKomentarzCardComponent } from '../../components/add-komentarz-card/add-komentarz-card.component';
import { TypKomentarza } from '../../enums/TypKomentarza';

@Component({
  selector: 'app-posty-page',
  standalone: true,
  imports: [CommonModule, PostCardComponent, KomentarzCardComponent, AddKomentarzCardComponent],
  templateUrl: './posty-page.component.html',
  styleUrl: './posty-page.component.css'
})
export class PostyPageComponent implements OnInit {
  post : PostResponse = {};
  postId: string | undefined;
  typ: TypKomentarza = TypKomentarza.POST;

  private _postObraz: string | undefined;

  errorMessage: string | null = null;

  public TypKomentarza = TypKomentarza;
  constructor(
    private route: ActivatedRoute,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.postId = params['postId'];
      if (this.postId) {
        this.getPostByPostId(this.postId);
        this.route.snapshot.data['postId'] = this.postId;
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
