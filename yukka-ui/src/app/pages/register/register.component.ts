import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegistrationRequest } from '../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services/authentication.service';
import { TokenService } from '../../services/token/token.service';
import { register } from '../../services/fn/authentication/register';
import { ErrorHandlingService } from '../../services/error-handler/error-handling.service';
import { ErrorMsgComponent } from "../../components/error-msg/error-msg.component";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerRequest: RegistrationRequest = {
    nazwa: 'TestowanyTestowiec24',
    email: 'beb@email.pl',
    haslo: 'testowiec12345678',
    powtorzHaslo: 'testowiec12345678'
  };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService
  ) {

  }

  register () {
    this.errorMsg = [];
    this.authService.register({
      body: this.registerRequest
    }).subscribe( {
        next: (res) => {
          console.log(res);
          this.router.navigate(['aktywacja-konta']);
          //this.login();
        },
        error: (err) => {
          console.log(err);
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
    });
  }

}
