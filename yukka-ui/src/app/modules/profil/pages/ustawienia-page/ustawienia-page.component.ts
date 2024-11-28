import { Component, OnInit } from '@angular/core';
import { UzytkownikService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { get } from 'http';
import { UzytkownikResponse } from '../../../../services/models/uzytkownik-response';
import { Ustawienia, UstawieniaRequest } from '../../../../services/models';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";

@Component({
  selector: 'app-ustawienia-page',
  standalone: true,
  imports: [CommonModule, FormsModule, EdycjaNavComponent],
  templateUrl: './ustawienia-page.component.html',
  styleUrl: './ustawienia-page.component.css'
})
export class UstawieniaPageComponent  implements OnInit {
  uzyt: UzytkownikResponse = {};
  ustawienia : UstawieniaRequest = {
    ogrodPokaz: false,
    powiadomieniaKomentarzeOdpowiedz: false,
    powiadomieniaOgrodKwitnienie: false,
    powiadomieniaOgrodOwocowanie: false,
    statystykiProfilu: false
  };

  nazwa: string | undefined;

  constructor(private uzytService : UzytkownikService,
      private tokenService: TokenService,
      private router: Router,
      private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      if (this.nazwa) {
        this.getUzytkownik(this.nazwa);
        this.route.snapshot.data['nazwa'] = this.nazwa;
      }
    });

  }

  getUzytkownik(nazwa: string)  {
    this.uzytService.findByNazwa( {nazwa: this.tokenService.nazwa } )
    .subscribe({
      next: (uzyt) => {
        console.log(uzyt);
        this.uzyt = uzyt;
        this.ustawienia = this.convertToUstawieniaRequest(uzyt.ustawienia || {});
      },
      error: (err) => {
        console.log(err);
      }
    });
  }


  convertToUstawieniaRequest(ustawienia: Ustawienia): UstawieniaRequest {
    return {
      ogrodPokaz: ustawienia.ogrodPokaz ?? false,
      powiadomieniaKomentarzeOdpowiedz: ustawienia.powiadomieniaKomentarzeOdpowiedz ?? false,
      powiadomieniaOgrodKwitnienie: ustawienia.powiadomieniaOgrodKwitnienie ?? false,
      powiadomieniaOgrodOwocowanie: ustawienia.powiadomieniaOgrodOwocowanie ?? false,
      statystykiProfilu: ustawienia.statystykiProfilu ?? false
    };
  }


  updatePowiadomienia() {
    if(this.ustawienia) {
      console.log("Masz tu ustawienia mordo: ");
      console.log(this.ustawienia);
      this.uzytService.updateUstawienia({body:  { ustawienia: this.ustawienia }})
      .subscribe({
        next: (uzyt) => {

          this.uzyt = uzyt;
          this.ustawienia = this.convertToUstawieniaRequest(uzyt.ustawienia || {});
          console.log(uzyt.ustawienia);
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
  }

}
