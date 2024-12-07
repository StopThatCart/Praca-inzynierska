import { Component, ViewChild } from '@angular/core';
import { ErrorMsgComponent } from "../../components/error-msg/error-msg.component";
import { CommonModule } from '@angular/common';
import { CodeInputComponent, CodeInputModule } from 'angular-code-input';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService, UzytkownikService } from '../../services/services';
import { ErrorHandlingService } from '../../services/error-handler/error-handling.service';
import {skipUntil} from 'rxjs';
import { HasloRequest } from '../../services/models';

@Component({
  selector: 'app-zmiana-hasla',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent, CodeInputModule],
  templateUrl: './zmiana-hasla.component.html',
  styleUrl: './zmiana-hasla.component.css'
})
export class ZmianaHaslaComponent {
  errorMsg: Array<string> = [];
  message = '';

  isDone: boolean = false;

  request : HasloRequest = {
    token: '',
    noweHaslo: '',
    nowePowtorzHaslo: '',
  };

  @ViewChild(CodeInputComponent) codeInput !: CodeInputComponent;

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  changePassword() {
    this.errorMsg = [];
    this.message = '';

    this.authService.changePassword({
      body: this.request
    }).subscribe({
      next: () => {
        this.message = 'Twoje hasło zostało aktywowane.\nMożesz się zalogować.';
        this.isDone = true;
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        this.codeInput.reset();
      }
    });
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  onCodeCompleted(token: string) {
    this.request.token = token;
  }

  protected readonly skipUntil = skipUntil;

}
