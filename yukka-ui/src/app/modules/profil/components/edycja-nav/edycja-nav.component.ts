import { Component, OnInit } from '@angular/core';
import { UzytkownikResponse } from '../../../../services/models';
import { TokenService } from '../../../../services/token/token.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UzytkownikService } from '../../../../services/services';

@Component({
  selector: 'app-edycja-nav',
  standalone: true,
  imports: [],
  templateUrl: './edycja-nav.component.html',
  styleUrl: './edycja-nav.component.css'
})
export class EdycjaNavComponent implements OnInit {
  private _avatar: string | undefined;

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute,
    private uzytService: UzytkownikService
  ) {}

  ngOnInit(): void {
    this.loadAvatar();

  }

  private loadAvatar() {
     if (this.tokenService.isTokenValid()) {
      this.uzytService.getAvatar().subscribe({
         next: (uzyt) => {
           this._avatar = uzyt.avatar;
         }
      });
    }
  }

  getUzytAvatar(): string | undefined {
    if(this._avatar) {
      return 'data:image/jpeg;base64,' + this._avatar;
    } else {
      this.loadAvatar();
    }
    return this._avatar;
  }

  goToProfil() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`/profil/${nazwa}`]);
    }
  }

  goToPowiadomienia() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      console.log('goToPowiadomieniaPage - start');
      this.router.navigate([`/profil/${nazwa}/powiadomienia`]);
    }
  }

  goToRozmowy() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`profil/${nazwa}/rozmowy`]);
    }
  }

  goToUstawienia() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`profil/${nazwa}/ustawienia`]);
    }
  }

  goToAvatarEdycja() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`profil/${nazwa}/edycja/avatar`]);
    }
  }



}
