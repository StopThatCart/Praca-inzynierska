import { Component, ElementRef, ViewChild } from '@angular/core';
import { PostRequest } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostService } from '../../../../services/services';
import { Router } from '@angular/router';

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

  constructor(private postService : PostService, private router : Router) { }


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
    if(this.request.obraz === '') {
      this.postService.addPost1$Json({
        body: this.request
      }).subscribe( {
          next: (res) => { this.goToPost(res.postId); },
          error: (err) => { this.handleErrors(err); }
      });
    }else {
      this.postService.addPost1$FormData({
        body: { request: this.request, file: this.wybranyPlik }
      }).subscribe( {
          next: (res) => { this.goToPost(res.postId); },
          error: (err) => { this.handleErrors(err); }
      });
    }
  }

  private goToPost(postId: string | undefined) {
    if (postId) {
      this.router.navigate(['/posty', postId]);
    } else {
      console.log('Error: Brak postId w response');
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
