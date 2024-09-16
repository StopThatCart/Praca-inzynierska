import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KomentarzRequest } from '../../../../services/models/komentarz-request';
import { KomentarzService } from '../../../../services/services';
import { Router } from '@angular/router';
import { TypKomentarza } from '../../enums/TypKomentarza';

@Component({
  selector: 'app-add-komentarz-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
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

  errorMsg: Array<string> = [];

  public TypKomentarza = TypKomentarza;

  constructor(private komentarzService : KomentarzService, private router : Router) { }

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
    if(this.request.obraz === '') {
      this.komentarzService.addKomentarzToPost1$Json({
        body: this.request
      }).subscribe( {
          next: (res) => {
         //   console.log(res);
            window.location.reload();
          },
          error: (err) => { this.handleErrors(err); }
      });
    }else {
      this.komentarzService.addKomentarzToPost1$FormData({
        body: { request: this.request, file: this.wybranyPlik }
      }).subscribe( {
          next: (res) => {
         //   console.log(res);
            window.location.reload();
          },
          error: (err) => { this.handleErrors(err); }
      });
    }
  }

  private addOdpowiedzToKomentarz() {
    if(this.request.obraz === '') {
      this.komentarzService.addOdpowiedzToKomentarz1$Json$Json ({
        body: this.request
      }).subscribe( {
          next: (res) => { window.location.reload(); },
          error: (err) => { this.handleErrors(err); }
      });
    }else {
      this.komentarzService.addOdpowiedzToKomentarz1$FormData$Json({
        body: { request: this.request, file: this.wybranyPlik }
      }).subscribe( {
          next: (res) => { window.location.reload(); },
          error: (err) => { this.handleErrors(err); }
      });
    }
  }


  private addWiadomoscToRozmowaPrywatna() {
    if(this.request.obraz === '') {
      this.komentarzService.addKomentarzToWiadomoscPrywatna1$Json({
        body: this.request
      }).subscribe( {
          next: (res) => { this.updateRozmowa(res); },
          error: (err) => { this.handleErrors(err); }
      });
    } else {
      this.komentarzService.addKomentarzToWiadomoscPrywatna1$FormData({
        body: { request: this.request, file: this.wybranyPlik }
      }).subscribe( {
          next: (res) => { this.updateRozmowa(res); },
          error: (err) => { this.handleErrors(err); }
      });
    }
  }

  private updateRozmowa(newMessage: any) {
    this.onNewMessage.emit(newMessage);
  }

  cancelOdpowiedz() {
    this.onCancelOdpowiedz.emit();
  }



  private handleErrors(err: any) {
    console.log(err);
    if(err.error.validationErrors) {
      this.errorMsg = err.error.validationErrors
    } else if (err.error.error) {
      this.errorMsg.push(err.error.error);
    } else {
      this.errorMsg.push(err.message);
    }
  }

}
