import { Component, ViewChild } from '@angular/core';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { EmailRequest } from '../../../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService, UzytkownikService } from '../../../../services/services';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { CodeInputComponent, CodeInputModule } from 'angular-code-input';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edycja-email-page',
  standalone: true,
  imports: [CommonModule, FormsModule, CodeInputModule, ErrorMsgComponent, EdycjaNavComponent],
  templateUrl: './edycja-email-page.component.html',
  styleUrl: './edycja-email-page.component.css'
})
export class EdycjaEmailPageComponent {
  errorMsg: Array<string> = [];
  message = '';

  emailSent: boolean = false;
  isDone: boolean = false;

  token: string = '';
  request : EmailRequest = {
    nowyEmail: '',
    haslo: '',
  };

  @ViewChild(CodeInputComponent) codeInput !: CodeInputComponent;

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private uzytService: UzytkownikService,
    private errorHandlingService: ErrorHandlingService
  ) {}


  sendEmailCode() {
    this.errorMsg = [];
    this.message = '';

    this.uzytService.sendZmianaEmail({
      body: this.request
    }).subscribe({
      next: () => {
        this.message = 'Na Twój nowy adres email został wysłany kod potwierdzający.';
        this.emailSent = true;
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
       // this.codeInput.reset();
      }
    });
  }

  changeEmail() {
    this.errorMsg = [];
    this.message = '';

    this.authService.zmianaEmail({
      token: this.token
    }).subscribe({
      next: () => {
        this.message = 'Twój adres email został zmieniony.';
        this.isDone = true;
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        this.codeInput.reset();
      }
    });
  }

  goBack() {
    this.emailSent = false;
    this.codeInput.reset();
    this.token = '';
  }

  onCodeCompleted(token: string) {
    this.token = token;
  }
}
