import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PostResponse } from '../../../../services/models/post-response';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../../../../services/services';
import { PostCardComponent } from "../../components/post-card/post-card.component";
import { KomentarzCardComponent } from '../../components/komentarz-card/komentarz-card.component';
import { AddKomentarzCardComponent } from '../../components/add-komentarz-card/add-komentarz-card.component';
import { TypKomentarza } from '../../models/TypKomentarza';
import { error } from 'console';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { LoadingComponent } from "../../../../components/loading/loading.component";

@Component({
  selector: 'app-posty-page',
  standalone: true,
  imports: [CommonModule, PostCardComponent, KomentarzCardComponent, AddKomentarzCardComponent, LoadingComponent],
  templateUrl: './posty-page.component.html',
  styleUrl: './posty-page.component.css'
})
export class PostyPageComponent implements OnInit {
  post : PostResponse = {};
  uuid: string | undefined;
  typ: TypKomentarza = TypKomentarza.POST;

  private _postObraz: string | undefined;

  errorMsg: Array<string> = [];
  isLoading: boolean = true;

  public TypKomentarza = TypKomentarza;
  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.uuid = params['uuid'];
      if (this.uuid) {
        this.getPostByUuid(this.uuid);
        this.route.snapshot.data['uuid'] = this.uuid;
      }
    });

  }

  getPostByUuid(uuid: string): void {
    this.postService.findPostByUuid({ uuid: uuid }).subscribe({
      next: (post) => {
        this.post = post;
        this.errorMsg = [];
      },
      error: (err) => {
        if (err.status === 404) {

        }
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
      },
      complete: () => {
        this.isLoading = false;
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
