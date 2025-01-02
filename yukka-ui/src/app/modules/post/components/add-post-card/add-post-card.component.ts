import { Component, ElementRef, ViewChild } from '@angular/core';
import { PostRequest } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostService } from '../../../../services/services';
import { Router } from '@angular/router';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-add-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-post-card.component.html',
  styleUrl: './add-post-card.component.css'
})
export class AddPostCardComponent {

  @ViewChild('fileInput') fileInput!: ElementRef;
  request: PostRequest = {
    tytul: '',
    opis: '',
    obraz: ''
  };
  wybranyObraz: any;
  wybranyPlik: any;

  errorMsg: Array<string> = [];

  constructor(
    private postService : PostService,
    private router : Router,
    private errorHandlingService: ErrorHandlingService
  ) { }


  onFileSelected(event: any) {
    this.wybranyPlik = event.target.files[0];

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


  addPost() {
    this.errorMsg = [];
    let leFile = null;
    if(this.request.obraz !== '') {
      leFile = this.wybranyPlik;
    }

    this.postService.addPost({
      body: { request: this.request, file: leFile }
    }).subscribe( {
        next: (res) => { this.goToPost(res.postId); },
        error: (error) => {  
          if (error.status === 403) {
            this.router.navigate(['/login']);
          }
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg); 
        }
    });

  }

  private goToPost(postId: string | undefined) {
    if (postId) {
      this.router.navigate(['/social/posty', postId]);
    } else {
      console.log('Error: Brak postId w response');
    }
  }

}
