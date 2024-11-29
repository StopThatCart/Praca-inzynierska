import { Component, inject, Input, TemplateRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ErrorMsgComponent } from '../../../../components/error-msg/error-msg.component';
import { ModalDismissReasons, NgbCalendar, NgbDatepickerModule, NgbModal, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { TokenService } from '../../../../services/token/token.service';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { PracownikService } from '../../../../services/services';
import { BanRequest } from '../../../../services/models';
import modal from 'bootstrap/js/dist/modal';
import { Router } from '@angular/router';


@Component({
  selector: 'app-ban-button',
  standalone: true,
  imports: [FormsModule, ErrorMsgComponent, NgbTooltipModule, NgbDatepickerModule],
  templateUrl: './ban-button.component.html',
  styleUrl: './ban-button.component.css'
})
export class BanButtonComponent {

  @Input() zglaszany: string | undefined;
  @Input() zbanowany: boolean | undefined;

  errorMsg: Array<string> = [];
  message: string = '';

  private modalService = inject(NgbModal);
	closeResult = '';

  request: BanRequest = {
    ban: true,
    banDo: '',
    nazwa: '',
    powod: ''
  };

  today = inject(NgbCalendar).getToday();

  constructor(
    private pracownikService: PracownikService,
    private tokenService: TokenService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) {}

  open(content: TemplateRef<any>) {
		this.modalService.open(content, { ariaLabelledBy: 'modal-ban' }).result.then(
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


  zbanujUzytkownika() {
   // console.log('Zgłoszenie użytkownika');
   // console.log(this.request);
    if (this.zglaszany === undefined ||
      this.zbanowany === undefined ||
      this.request.banDo === '' ||
      this.request.powod === '')
      return;

    this.request.nazwa = this.zglaszany;
    this.request.ban = !this.zbanowany;

    this.errorMsg = [];
    this.message = '';
    console.log('Zgłoszenie użytkownika');

    this.pracownikService.setBanUzytkownik( { body: { request: this.request } } ).subscribe({
      next: (res) => {
        window.location.reload();
        this.modalService.dismissAll();
      },
      error: (err) => {
        console.error('Błąd podczas wysyłania zgłoszenia');
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
      }
    });
  }


  showAlert() {
    if(!confirm("Czy na pewno chcesz odbanować tego użytkownika?")) {
      return;
    }

    if(this.zglaszany === undefined) return;
    this.pracownikService.unbanUzytkownik({ 'uzytkownik-nazwa': this.zglaszany }).subscribe({
      next: (res) => {
        console.log('Użytkownik odbanowany');
        window.location.reload();
      },
      error: (err) => {
        console.error('Błąd podczas odbanowywania użytkownika');
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
      }
    });
  }
}
