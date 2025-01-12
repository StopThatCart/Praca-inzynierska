import { Component, Input } from '@angular/core';
import { KomentarzResponse } from '../../../../services/models';
import { Router } from '@angular/router';
import { KomentarzService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-simple-komentarz-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './simple-komentarz-card.component.html',
  styleUrl: './simple-komentarz-card.component.css'
})
export class SimpleKomentarzCardComponent {

  @Input()  komentarz:KomentarzResponse = {};

  private _komentarzObraz: string | undefined;
  private _komentarzAvatar: string | undefined;

  constructor(
    private router: Router,
    private komentarzService: KomentarzService,
    private tokenService: TokenService) {}

  getKomentarz(): KomentarzResponse {
    return this.komentarz;
  }

  setKomentarz(komentarz: KomentarzResponse) {
    this.komentarz = komentarz;
  }

  getKomentarzObraz(): string | undefined {
    if(this.komentarz.obraz) {
      return 'data:image/jpeg;base64,' + this.komentarz.obraz;
    }
    return this._komentarzObraz;
  }

  getKomentarzAvatar(): string | undefined {
    if(this.komentarz.avatar) {
      return 'data:image/jpeg;base64,' + this.komentarz.avatar;
    }
    return this._komentarzAvatar;
  }

  goToProfil() {
    if (this.komentarz.uzytkownikNazwa) {
      this.router.navigate([`/profil/${this.komentarz.uzytkownikNazwa}`]);
    }
  }

  goToPost() {
    if (this.komentarz.post && this.komentarz.post.uuid) {
      this.router.navigate([`/social/posty/${this.komentarz.post.uuid}`]);
    }
  }
}
