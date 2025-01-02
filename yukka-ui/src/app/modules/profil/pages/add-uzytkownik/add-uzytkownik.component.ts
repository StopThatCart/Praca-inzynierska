import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PracownikService, UzytkownikService } from '../../../../services/services';
import { Router } from '@angular/router';
import { ErrorMsgComponent } from '../../../../components/error-msg/error-msg.component';
import { RegistrationRequest } from '../../../../services/models';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-add-uzytkownik',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent],
  templateUrl: './add-uzytkownik.component.html',
  styleUrl: './add-uzytkownik.component.css'
})
export class AddUzytkownikComponent {
  request: RegistrationRequest = {
    nazwa: '',
    email: '',
    haslo: '',
    powtorzHaslo: ''
  };

  errorMsg: Array<string> = [];
  message: string = '';

  constructor(
    private uzytService: UzytkownikService,
    private pracownikService: PracownikService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) {}

  onSubmit() {
    this.errorMsg = [];
    this.message = '';

    this.pracownikService.addPracownik({ body: { request: this.request } }).subscribe( {
        next: (res) => {
          console.log(res);
          this.message = 'Użytkownik został dodany, a na podany adres email został wysłany kod aktywacyjny.';
          //this.router.navigate(['aktywacja-konta']);
          //this.login();
        },
        error: (err) => {
          console.log(err);
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
    });
  
  }
}
