import { Component, OnInit } from '@angular/core';
import { Uzytkownik, UzytkownikResponse } from '../../../../services/models';
import { ActivatedRoute } from '@angular/router';
import { UzytkownikService } from '../../../../services/services';

@Component({
  selector: 'app-profil-page',
  standalone: true,
  imports: [],
  templateUrl: './profil-page.component.html',
  styleUrl: './profil-page.component.css'
})
export class ProfilPageComponent implements OnInit {
  uzyt: UzytkownikResponse = {};
  nazwa: string | undefined;

  private _avatar: string | undefined;

  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
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

  getOverallOceny(): number {
    if(this.uzyt.komentarzeOcenyPozytywne && this.uzyt.postyOcenyPozytywne) {
      return (this.uzyt.komentarzeOcenyPozytywne + this.uzyt.postyOcenyPozytywne);
    }
    return 0;
  }

}
