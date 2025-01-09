import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KomentarzRequest } from '../../../../services/models/komentarz-request';
import { KomentarzService, RozmowaPrywatnaService } from '../../../../services/services';
import { Router } from '@angular/router';
import { TypKomentarza } from '../../models/TypKomentarza';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";

@Component({
  selector: 'app-add-komentarz-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent],
  templateUrl: './add-komentarz-card.component.html',
  styleUrls: ['./add-komentarz-card.component.css']
})
export class AddKomentarzCardComponent implements OnInit {
  @Input() targetId: string | undefined;
  @Input() typ: TypKomentarza | undefined;
  @Output() onCancelOdpowiedz = new EventEmitter<void>();
  @Output() onNewMessage = new EventEmitter<any>();


  @ViewChild('fileInput') fileInput!: ElementRef;
  request: KomentarzRequest = {
    opis: '',
    targetId: '',
    obraz: ''
  };
  wybranyObraz: any;
  wybranyPlik: any;

  errorMsg: string[] = [];

  public TypKomentarza = TypKomentarza;

  constructor(
    private komentarzService : KomentarzService, 
    private rozmowaPrywatnaService: RozmowaPrywatnaService,
    private router : Router,
    private errorHandlingService: ErrorHandlingService
  ) { }

  ngOnInit() {
    if(this.targetId) {
      console.log("TargetId przy załadowaniu: " + this.targetId);
      this.request.targetId = this.targetId;
    }

    console.log("Request przy załadowaniu");
    console.log(this.request);
  }


  onFileSelected(event: any) {
    // console.log("Obraz wybrano");
    this.wybranyPlik = event.target.files[0];
    //console.log(file);

    if (this.wybranyPlik) {
      this.request.obraz = this.wybranyPlik.name;

      const reader = new FileReader();
      reader.onload = () => {
        this.wybranyObraz = reader.result as string;
      };
      reader.readAsDataURL(this.wybranyPlik);
    }
  }

  clearImage() {
    this.wybranyObraz = null;
    this.wybranyPlik = null;
    this.request.obraz = '';
    this.fileInput.nativeElement.value = '';
  }


  addKomentarz() {
    console.log("Request przy dodawaniu");
    console.log(this.request);
    this.errorMsg = [];

    if(this.typ) {
      if(this.typ === TypKomentarza.ODPOWIEDZ) {
        console.log("Odpowiedz na komentarz");
        this.addOdpowiedzToKomentarz();
      } else if(this.typ === TypKomentarza.POST) {
        console.log("Komentarz na post");
        this.addKomentarzToPost();
      } else if(this.typ === TypKomentarza.WIADOMOSC) {
        console.log("Wiadomość do użytkownika");
        this.addWiadomoscToRozmowaPrywatna();
      }

    } else {
      console.log("Nie wybrano typu komentarza");
    }

  }

  private addKomentarzToPost() {
    let leFile = this.wybranyPlik;
    if(this.request.obraz === '') {
      leFile = null;
    }

    this.komentarzService.addKomentarzToPost({
      body: { request: this.request, file: leFile }
    }).subscribe( {
        next: (res) => {
          window.location.reload();
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
    });
  }

  private addOdpowiedzToKomentarz() {
    let leFile = this.wybranyPlik;
    if(this.request.obraz === '') {
      leFile = null;
    }

    this.komentarzService.addOdpowiedzToKomentarz({
      body: { request: this.request, file: leFile }
    }).subscribe( {
        next: (res) => { window.location.reload(); },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
    });
  }


  private addWiadomoscToRozmowaPrywatna() {
    let leFile = this.wybranyPlik;
    if(this.request.obraz === '') {
      leFile = null;
    }

    this.wybranyPlik = null;
    this.rozmowaPrywatnaService.addKomentarzToWiadomoscPrywatna({
      body: { request: this.request, file: leFile }
    }).subscribe( {
        next: (res) => { this.updateRozmowa(res); },
        error: (err) => { this.errorHandlingService.handleErrors(err, this.errorMsg); }
    });

  }

  private updateRozmowa(newMessage: any) {
    this.request.opis = '';
    this.clearImage();
    this.onNewMessage.emit(newMessage);
  }

  cancelOdpowiedz() {
    this.onCancelOdpowiedz.emit();
  }


}
