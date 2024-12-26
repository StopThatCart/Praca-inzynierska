import { Component, inject, Input, TemplateRef } from '@angular/core';
import { PowiadomienieService } from '../../../../services/services/powiadomienie.service';
import { TokenService } from '../../../../services/token/token.service';
import { ZgloszenieRequest } from '../../../../services/models';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ModalDismissReasons, NgbDatepickerModule, NgbModal, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { TypPowiadomienia } from '../../models/TypPowiadomienia';
import { Router } from '@angular/router';

@Component({
  selector: 'app-zgloszenie-button',
  standalone: true,
  imports: [FormsModule, ErrorMsgComponent, NgbTooltipModule],
  templateUrl: './zgloszenie-button.component.html',
  styleUrl: './zgloszenie-button.component.css'
})
export class ZgloszenieButtonComponent {

  @Input() typPowiadomienia: TypPowiadomienia | undefined;
  @Input() zglaszany: string | undefined;
  @Input() odnosnik: string | undefined;

  errorMsg: Array<string> = [];
  message: string = '';

  private modalService = inject(NgbModal);
	closeResult = '';

  request: ZgloszenieRequest = {
    typPowiadomienia: '',
    zglaszany: '',
    odnosnik: '',
    opis: ''
  };

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

		this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
			(result) => {
				this.closeResult = `Closed with: ${result}`;
			},
			(reason) => {
				this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
			},
		);
	}

  private getDismissReason(reason: any): string {
		switch (reason) {
			case ModalDismissReasons.ESC:
				return 'by pressing ESC';
			case ModalDismissReasons.BACKDROP_CLICK:
				return 'by clicking on a backdrop';
			default:
				return `with: ${reason}`;
		}
	}


  zglosUzytkownika() {
   // console.log('Zgłoszenie użytkownika');
   // console.log(this.request);
    //console.log(this.zglaszany);
    if (this.typPowiadomienia === undefined ||
      this.zglaszany === undefined ||
      this.odnosnik === undefined ||
      this.request.opis === '')
      return;

    this.request.typPowiadomienia = this.typPowiadomienia.toString();
    this.request.zglaszany = this.zglaszany;
    this.request.odnosnik = this.odnosnik;

    this.errorMsg = [];
    this.message = '';
    console.log('Zgłoszenie użytkownika');

    this.powiadomienieService.sendZgloszenie( { body: this.request } ).subscribe({
      next: (res) => {
        console.log('Zgłoszenie zostało wysłane');
        this.message = 'Zgłoszenie zostało wysłane';
        //modal.close('Save click')
      },
      error: (err) => {
        console.error('Błąd podczas wysyłania zgłoszenia');
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
      }
    });
  }
}
