import { Component, OnInit } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { ProfilRequest } from '../../../../services/models';
import { UzytkownikService } from '../../../../services/services';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-edycja-profil-page',
  standalone: true,
  imports: [CommonModule, FormsModule, EdycjaNavComponent, ErrorMsgComponent],
  templateUrl: './edycja-profil-page.component.html',
  styleUrl: './edycja-profil-page.component.css'
})
export class EdycjaProfilPageComponent implements OnInit {

  errorMsg: Array<string> = [];
  message = '';
  nazwa: string = '';

  request: ProfilRequest = {
    imie: undefined,
    miasto: undefined,
    miejsceZamieszkania: undefined,
    opis: undefined
  };


  constructor(
    private tokenService: TokenService,
    private uzytService: UzytkownikService,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      this.uzytService.findByNazwa({ nazwa: params['nazwa'] })
      .subscribe({
        next: (response) => {
          this.request.imie = response.imie;
          this.request.miasto = response.miasto;
          this.request.miejsceZamieszkania = response.miejsceZamieszkania;
          this.request.opis = response.opis;
        },
        error: (error) => {
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        }
      });
    });
  }

  editProfil() {
    this.errorMsg = [];
    this.message = '';

    this.uzytService.updateProfil({body: { profil: this.request} }).subscribe({
      next: () => {
        this.message = 'Pomyślnie zaktualizowano profil';
        this.router.navigate(['profil', this.nazwa]);
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });

  }
}
