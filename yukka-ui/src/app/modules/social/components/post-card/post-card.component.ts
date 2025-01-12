import { Component, Input, OnInit } from '@angular/core';
import { PostResponse } from '../../../../services/models';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../../services/services';
import { OcenaRequest } from '../../../../services/models/ocena-request';
import { HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../../../services/token/token.service';
import { ZgloszenieButtonComponent } from "../../../profil/components/zgloszenie-button/zgloszenie-button.component";
import { TypPowiadomienia } from '../../../profil/models/TypPowiadomienia';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

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
  constructor(
    private router: Router,
    private postService: PostService,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService
  ) {}

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



  addOcenaToPost(uuid: string | undefined, ocena: boolean) {
    if (uuid && this.tokenService) {

      // if(this.tokenService.nazwa === this.post.uzytkownik) {
      //   return;
      // }

      let ocenaRequest: OcenaRequest = { lubi: ocena, ocenialnyId: uuid };
      this.postService.addOcenaToPost({ body: ocenaRequest }).subscribe({
        next: (res) => {
          console.log('Ocena dodana: ', res);
          console.log('Oceny: ', res.ocenyLubi, res.ocenyNieLubi);
          this.post.ocenyLubi = res.ocenyLubi;
          this.post.ocenyNieLubi = res.ocenyNieLubi;
          console.log('Post po ocenie: ', this.post);
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

  deletePost() {
    this.errorMsg = [];
    if(confirm("Czy aby na pewno chcesz usunąć ten post?")) {
      if (this.post.uuid) {
        this.postService.removePost({ uuid: this.post.uuid }).subscribe({
          next: (res) => {
            //console.log('Post usunięty');
            this.router.navigate(['/social/posty']);
          },
          error: (error) => {
            this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
          }
        });
      }
    }
  }

}
