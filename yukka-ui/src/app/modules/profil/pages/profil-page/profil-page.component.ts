import { Component, OnInit } from '@angular/core';
import { Uzytkownik, UzytkownikResponse } from '../../../../services/models';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService, UzytkownikService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule } from '@angular/common';
import { RozmowaPrywatnaService } from '../../../../services/services/rozmowa-prywatna.service';

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

  zaproszony: boolean | undefined;
  isZaproszonyChecked: boolean = false;
  zaproszenieWyslane: boolean = false;

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute,
    private postService: PostService,
    private uzytService: UzytkownikService,
    private rozService: RozmowaPrywatnaService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      if (this.nazwa) {
        this.getUzytkownikByNazwa(this.nazwa);
        this.route.snapshot.data['nazwa'] = this.nazwa;
        this.postyCount = this.getPostyCount();
        this.oceny = this.getOverallOceny();
      }
    });
    this.isCurrentUserBoi =   this.isCurrentUser();
  }

  getUzytkownikByNazwa(nazwa: string): void {
    this.uzytService.findByNazwa({ nazwa: nazwa }).subscribe({
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
    if(this.nazwa) {
      this.postService.findAllPostyCountOfUzytkownik({ nazwa: this.nazwa }).subscribe({
        next: (count) => {
          console.log('Posty count: ', count);
          this.postyCount = count;
          return this.postyCount;
        },
        error: (err) => {
          console.log('Error: ', err);
          return 0;
        }
      });
    }
    return 0;
  }

  getOverallOceny(): number {
    if(!this.uzyt) {
      this.oceny = 0;
    }
    if(this.uzyt?.komentarzeOcenyPozytywne !== undefined && this.uzyt?.postyOcenyPozytywne !== undefined) {
      this.oceny = (this.uzyt.komentarzeOcenyPozytywne + this.uzyt.postyOcenyPozytywne);
    }
    return this.oceny;
  }

  isCurrentUser(): boolean {
    if(this.tokenService) {
      if(this.tokenService.nazwa === this.uzyt.nazwa) {
        return true;
      }
    }
    return false;
  }


  checkIfZaproszony(): void {
    if(this.zaproszony) {
      return;
    }
  }

  isZaproszony(): boolean {
   // console.log('Is zaproszony: ', this.zaproszony);
    if (this.isZaproszonyChecked) {
      return this.zaproszony || false;
    }

    if (this.zaproszony) {
     // console.log('Zaproszony: ', this.zaproszony);
      this.isZaproszonyChecked = true;
      return this.zaproszony;
    }

    if (this.tokenService.token && this.uzyt.nazwa
      && this.tokenService.nazwa !== this.uzyt.nazwa) {

    //  console.log('Check if zaproszony');
      this.rozService.getRozmowaPrywatna({ 'uzytkownik-nazwa': this.uzyt.nazwa })
        .subscribe({
          next: (roz) => {
           // console.log('Rozmowa: ', roz);
            if (roz && !roz.aktywna) {
              this.zaproszony = true;
            }
            this.isZaproszonyChecked = true;
            return this.zaproszony;
          },
          error: (err) => {
            if (err.status === 404) {
              this.isZaproszonyChecked = true;
              return null;
            }
            this.isZaproszonyChecked = true;
            return false;
          }
      });
    }
    return false;
  }


  zaprosDoRozmowaPrywatna() {
    if (this.uzyt.nazwa) {
      this.rozService.inviteToRozmowaPrywatna({ 'uzytkownik-nazwa': this.uzyt.nazwa })
        .subscribe({
          next: (roz) => {
            if(roz) {
              console.log('Zaproszenie do rozmowy prywatnej wysłane: ', roz);
              this.zaproszenieWyslane = true;
            } else {
              console.log('Coś poszło nie tak.');
            }
          }
        });
    }
  }

  goToRozmowa() {
    this.router.navigate(['rozmowy', this.uzyt.nazwa], { relativeTo: this.route });
    //this.router.navigate(['profil/rozmowy', this.uzyt.nazwa]);
  }



  goToRozmowy() {
    this.router.navigate(['rozmowy'], { relativeTo: this.route });
    //this.router.navigate(['profil/rozmowy']);
  }

}
