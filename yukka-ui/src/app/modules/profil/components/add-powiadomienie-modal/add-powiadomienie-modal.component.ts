import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, inject, Input, Output, TemplateRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Modal } from 'bootstrap';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { PowiadomienieService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';

import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { TypPowiadomienia } from '../../models/TypPowiadomienia';
import { SpecjalnePowiadomienieRequest, ZgloszenieRequest } from '../../../../services/models';

@Component({
  selector: 'app-add-powiadomienie-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent],
  templateUrl: './add-powiadomienie-modal.component.html',
  styleUrl: './add-powiadomienie-modal.component.css'
})
export class AddPowiadomienieModalComponent {
  errorMsg: Array<string> = [];
  message: string = '';

  private modalService = inject(NgbModal);

  request: SpecjalnePowiadomienieRequest = {
    opis: ''
  };

  doKogo: string = 'uzytkownik';

  @Output() powiadomienieDodane = new EventEmitter<String>();

  constructor(
    private powiadomienieService: PowiadomienieService,
    private tokenService: TokenService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) {}

  open(content: TemplateRef<any>) {
    if(!this.tokenService.isTokenValid()) {
      this.router.navigate(['/login']);
      return;
    }

    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  onSubmit() {
    console.log('Wysyłanie powiadomienia');
    if (this.doKogo === undefined || this.request.opis === '') return;
    this.errorMsg = [];
    this.message = '';

    if (this.doKogo?.toLowerCase() === 'uzytkownik') {
      this.powiadomienieService.sendSpecjalnePowiadomienie( { body: this.request } ).subscribe({
        next: (res) => {
          console.log('Powiadomienie zostało wysłane');
          this.message = 'Powiadomienie zostało wysłane';
          this.powiadomienieDodane.emit();
          this.modalService.dismissAll();
        },
        error: (err) => {
          console.error('Błąd podczas wysyłania powiadomienia');
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
      });
    } else if (this.doKogo?.toLowerCase() === 'pracownik') {
      this.powiadomienieService.sendSpecjalnePowiadomienieToPracownicy( { body: this.request } ).subscribe({
        next: (res) => {
          console.log('Powiadomienie do pracowników zostało wysłane');
          this.message = 'Powiadomienie do pracowników zostało wysłane';
          this.powiadomienieDodane.emit();
          this.modalService.dismissAll();
        },
        error: (err) => {
          console.error('Błąd podczas wysyłania powiadomienia do pracowników');
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
      });
    } else {
      console.error('Nieznany adresat powiadomienia');
      this.errorMsg.push('Nieznany adresat powiadomienia');
    }
  }

  isAdmin(): boolean {
    return this.tokenService.isAdmin();
  }

}
