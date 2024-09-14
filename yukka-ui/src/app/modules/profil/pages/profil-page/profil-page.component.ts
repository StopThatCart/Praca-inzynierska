import { Component, OnInit } from '@angular/core';
import { Uzytkownik, UzytkownikResponse } from '../../../../services/models';
import { ActivatedRoute } from '@angular/router';
import { PostService, UzytkownikService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profil-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profil-page.component.html',
  styleUrl: './profil-page.component.css'
})
export class ProfilPageComponent implements OnInit {
  uzyt: UzytkownikResponse = {};
  nazwa: string | undefined;
  private _avatar: string | undefined;

  errorMessage: string | null = null;

  isCurrentUserBoi: boolean = false;

  postyCount: number = 0;
  oceny: number = 0;


  constructor(
    private tokenService: TokenService,
    private route: ActivatedRoute,
    private postService: PostService,
    private uzytService: UzytkownikService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      if (this.nazwa) {
        this.getUzytkownikByNazwa(this.nazwa);
        this.route.snapshot.data['nazwa'] = this.nazwa;
      }
    });
    this.isCurrentUserBoi = this.isCurrentUser();
  //  this.postyCount = this.getPostyCount();
    this.oceny = this.getOverallOceny();

  }

  getUzytkownikByNazwa(nazwa: string): void {
    this.uzytService.findByNazwa({ 'nazwa': nazwa }).subscribe({
      next: (uzyt) => {
        this.uzyt = uzyt;
        this.errorMessage = null;
      },
      error: (err) => {
        this.errorMessage = 'Nie znaleziono użytkownika o podanej nazwie.';
      }
    });
  }


  getAvatar(): string | undefined {
    if(this.uzyt && this.uzyt.avatar) {
      return 'data:image/jpeg;base64,' + this.uzyt.avatar;
    }
    return this._avatar;
  }

  getPostyCount(): number {
    if(this.uzyt?.nazwa) {
      this.postService.findAllPostyCountOfUzytkownik({ nazwa: this.uzyt.nazwa }).subscribe({
        next: (count) => {
          return count;
        }
      });
    }
    return 0;
  }

  getOverallOceny(): number {
    if(this.uzyt?.komentarzeOcenyPozytywne !== undefined && this.uzyt?.komentarzeOcenyPozytywne >= 0
        && this.uzyt?.postyOcenyPozytywne !== undefined && this.uzyt?.postyOcenyPozytywne >= 0) {
      return (this.uzyt.komentarzeOcenyPozytywne + this.uzyt.postyOcenyPozytywne);
    }
    return 0;
  }


  isCurrentUser(): boolean {

    if(this.tokenService) {
      if(this.tokenService.nazwa === this.uzyt.nazwa) {
        return true;
      }
    }
    return false;
  }

}
