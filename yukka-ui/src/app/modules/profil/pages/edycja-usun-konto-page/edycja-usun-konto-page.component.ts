import { Component } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { UzytkownikService } from '../../../../services/services';
import { UsunKontoRequest } from '../../../../services/models';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { error } from 'console';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-edycja-usun-konto-page',
  standalone: true,
  imports: [CommonModule, FormsModule, EdycjaNavComponent, ErrorMsgComponent],
  templateUrl: './edycja-usun-konto-page.component.html',
  styleUrl: './edycja-usun-konto-page.component.css'
})
export class EdycjaUsunKontoPageComponent {

  errorMsg: Array<string> = [];
  message = '';

  request: UsunKontoRequest = {
    haslo: ''
  };

  constructor(
    private tokenService: TokenService,
    private uzytkownikService: UzytkownikService,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandlingService: ErrorHandlingService
  ) {}


  async usunKonto() {
    this.errorMsg = [];
    this.message = '';

    if(!confirm("Czy na pewno chcesz usunąć konto? Tej operacji nie można cofnąć!")) return;

    console.log(this.request);
    console.log("Usuwam konto");
    this.uzytkownikService.removeSelf({ body: this.request }).subscribe({
      next: () => {
        this.tokenService.clearToken();
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });

    // try {
    //   await this.uzytkownikService.removeSelf({ body: this.request }).toPromise();
    //   // this.tokenService.clearToken();
    //   // this.router.navigate(['/']);
    // } catch (error) {
    //   this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
    // }
  }



}
