import { Component, Input, OnInit } from '@angular/core';
import { PostResponse } from '../../../../services/models';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { addOcenaToPost } from '../../../../services/fn/post/add-ocena-to-post';
import { PostService } from '../../../../services/services';
import { OcenaRequest } from '../../../../services/models/ocena-request';
import { removeOcenaFromPost } from '../../../../services/fn/post/remove-ocena-from-post';
import { HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../../../services/token/token.service';
import { ZgloszenieButtonComponent } from "../../../profil/components/zgloszenie-button/zgloszenie-button.component";
import { TypPowiadomienia } from '../../../profil/enums/TypPowiadomienia';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, RouterModule, ZgloszenieButtonComponent, NgbTooltipModule],
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.css'
})
export class PostCardComponent implements OnInit {
  @Input()  post:PostResponse = {};
  private _postObraz: string | undefined;
  private _postAvatar: string | undefined;

  errorMsg: Array<string> = [];
  typPowiadomienia = TypPowiadomienia;
  constructor(private router: Router,
              private postService: PostService,
              private tokenService: TokenService) {}

  ngOnInit(): void {

  }

  canModify(): boolean {
    if(this.tokenService.isTokenValid()) {
      if (this.post.uzytkownik && this.tokenService.hasAuthenticationRights(this.post.uzytkownik)) {
        return true;
      }
    }
    return false;
  }


  getPost(): PostResponse {
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
    if (postId && this.tokenService) {

      if(this.tokenService.nazwa === this.post.uzytkownik) {
        return;
      }

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


  deletePost() {
    this.errorMsg = [];
    if(confirm("Czy aby na pewno chcesz usunąć ten post?")) {
      if (this.post.postId) {
        this.postService.removePost({ 'post-id': this.post.postId }).subscribe({
          next: (res) => {
            //console.log('Post usunięty');
            this.router.navigate(['/posty']);
          },
          error: (err) => {
            this.handleErrors(err);
          }
        });
      }
    }
  }


  private handleErrors(err: any) {
    console.log(err);
    if(err.error.validationErrors) {
      this.errorMsg = err.error.validationErrors
    } else if (err.error.error) {
      this.errorMsg.push(err.error.error);
    } else {
      this.errorMsg.push(err.message);
    }
  }

}
