import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {skipUntil} from 'rxjs';

import {CodeInputModule} from 'angular-code-input';
import { CommonModule } from '@angular/common';
import { ErrorMsgComponent } from "../../components/error-msg/error-msg.component";
import { error } from 'console';
import { ErrorHandlingService } from '../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-aktywacja-konta',
  standalone: true,
  imports: [CommonModule, CodeInputModule, ErrorMsgComponent],
  templateUrl: './aktywacja-konta.component.html',
  styleUrl: './aktywacja-konta.component.css'
})
export class AktywacjaKontaComponent {

  errorMsg: Array<string> = [];
  message = '';
  isOkay = true;
  submitted = false;
  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  private confirmAccount(token: string) {
    this.errorMsg = [];
    this.message = '';

    this.authService.confirm({
      token
    }).subscribe({
      next: () => {
        this.message = 'Twoje konto zostało aktywowane.\nMożesz się zalogować';
        this.submitted = true;
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        this.submitted = true;
        this.isOkay = false;
      }
    });
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  protected readonly skipUntil = skipUntil;
}
