import { CommonModule } from '@angular/common';
import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KomentarzRequest } from '../../../../services/models/komentarz-request';
import { KomentarzService } from '../../../../services/services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-komentarz-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-komentarz-card.component.html',
  styleUrls: ['./add-komentarz-card.component.css']
})
export class AddKomentarzCardComponent implements OnInit {
  @Input() targetId: string | undefined;
  @ViewChild('fileInput') fileInput!: ElementRef;
  request: KomentarzRequest = {
    opis: '',
    targetId: '',
    obraz: ''
  };
  wybranyObraz: any;
  wybranyPlik: any;

  errorMsg: Array<string> = [];

  constructor(private komentarzService : KomentarzService, private router : Router) { }

  ngOnInit() {
    if(this.targetId) {
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

  addKomentarzToPost() {
    console.log("Request przy dodawaniu");
    console.log(this.request);
    this.errorMsg = [];
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
