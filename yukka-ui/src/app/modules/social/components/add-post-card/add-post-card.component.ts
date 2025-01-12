import { Component, ElementRef, ViewChild } from '@angular/core';
import { PostRequest } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostService } from '../../../../services/services';
import { Router } from '@angular/router';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";

@Component({
  selector: 'app-add-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ImageUploadComponent],
  templateUrl: './add-post-card.component.html',
  styleUrl: './add-post-card.component.css'
})
export class AddPostCardComponent {
  request: PostRequest = {
    tytul: '',
    opis: '',
    obraz: ''
  };
  wybranyPlik: any;

  errorMsg: Array<string> = [];

  constructor(
    private postService : PostService,
    private router : Router,
    private errorHandlingService: ErrorHandlingService
  ) { }


  onFileSelected(file: File) {
    this.wybranyPlik = file;
    this.request.obraz = this.wybranyPlik.name;
  }

  clearImage() {
     this.wybranyPlik = null;
     this.request.obraz = '';
  }


  addPost() {
    this.errorMsg = [];
    let leFile = null;
    if(this.request.obraz !== '') {
      leFile = this.wybranyPlik;
    }

    this.postService.addPost({
      body: { request: this.request, file: leFile }
    }).subscribe( {
        next: (res) => { this.goToPost(res.uuid); },
        error: (error) => {  
          if (error.status === 403) {
            this.router.navigate(['/login']);
          }
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg); 
        }
    });

  }

  private goToPost(uuid: string | undefined) {
    if (uuid) {
      this.router.navigate(['/social/posty', uuid]);
    } else {
      console.log('Error: Brak uuid w response');
    }
  }

}
