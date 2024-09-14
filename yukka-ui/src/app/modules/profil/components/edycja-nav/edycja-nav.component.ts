import { Component, OnInit } from '@angular/core';
import { UzytkownikResponse } from '../../../../services/models';
import { TokenService } from '../../../../services/token/token.service';
import { ActivatedRoute } from '@angular/router';
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
  loggedIn: boolean = false;

  constructor(
    private tokenService: TokenService,
    private route: ActivatedRoute,
    private uzytService: UzytkownikService
  ) {}

  ngOnInit(): void {
    this.loadAvatar();
    this.tokenService.isLoggedIn.subscribe((status: boolean) => {
      this.loggedIn = status;
    });

    if (!this.tokenService.isTokenValid()) {
      this.loggedIn = false;
    }
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

}
