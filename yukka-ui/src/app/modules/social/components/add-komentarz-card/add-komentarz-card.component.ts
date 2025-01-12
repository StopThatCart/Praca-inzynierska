import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KomentarzRequest } from '../../../../services/models/komentarz-request';
import { KomentarzService, RozmowaPrywatnaService } from '../../../../services/services';
import { Router } from '@angular/router';
import { TypKomentarza } from '../../models/TypKomentarza';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";

@Component({
  selector: 'app-add-komentarz-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent, ImageUploadComponent],
  templateUrl: './add-komentarz-card.component.html',
  styleUrls: ['./add-komentarz-card.component.css']
})
export class AddKomentarzCardComponent implements OnInit {
  @Input() targetId: string | undefined;
  @Input() typ: TypKomentarza | undefined;
  @Output() onCancelOdpowiedz = new EventEmitter<void>();
  @Output() onNewMessage = new EventEmitter<any>();

  request: KomentarzRequest = {
    opis: '',
    targetId: '',
    obraz: ''
  };
  wybranyPlik: any;

  errorMsg: string[] = [];

  public TypKomentarza = TypKomentarza;

  constructor(
    private komentarzService : KomentarzService, 
    private rozmowaPrywatnaService: RozmowaPrywatnaService,
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

  onFileSelected(file: any) {
    this.wybranyPlik = file;
    this.request.obraz = this.wybranyPlik.name;
  }

  clearImage() {
    this.wybranyPlik = null;
    this.request.obraz = '';
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
