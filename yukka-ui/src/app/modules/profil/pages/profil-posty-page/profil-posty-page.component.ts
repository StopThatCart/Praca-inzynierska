import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { PostCardComponent } from '../../../post/components/post-card/post-card.component';
import { PageResponsePostResponse } from '../../../../services/models';
import { PostService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { LoadingComponent } from "../../../../components/loading/loading.component";

@Component({
  selector: 'app-profil-posty-page',
  standalone: true,
  imports: [CommonModule, FormsModule, PostCardComponent, InfiniteScrollModule, ErrorMsgComponent, EdycjaNavComponent, LoadingComponent],
  templateUrl: './profil-posty-page.component.html',
  styleUrl: './profil-posty-page.component.css'
})
export class ProfilPostyPageComponent {
  nazwa: string | undefined;

  postResponse: PageResponsePostResponse = {};
  isLoading = false;
  page = 1;
  size = 5;
  searchText = '';
  pages: number[] = [];

  toggleLoading: () => void;

  message = '';
  errorMsg: Array<string> = [];

  constructor(
    private postService: PostService,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandlingService: ErrorHandlingService
  ) {
    this.toggleLoading = () => this.isLoading = !this.isLoading;
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      if (this.nazwa) {
        this.findAllPosty();
        console.log(this.postResponse);
      }
    });

  }

  findAllPosty() {
    console.log('findAllPostyOfUzytkownik');
    if(!this.nazwa) return;
    this.errorMsg = [];

    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;
    this.toggleLoading();
    this.postService.findAllPostyByUzytkownik({
      page: this.page - 1,
      size: this.size,
      nazwa: this.nazwa
    }).subscribe({
        next: (posty) => {
          this.postResponse = posty;
        },
        error: (err) => {
          if (err.status === 403) {

            //console.log('Eaaaaa');
          }
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
          this.toggleLoading();
          //console.error('Error fetching posty:', err);
        },
        complete:()=> this.toggleLoading()
      });
  }

  appendPost= ()=>{
    if(!this.nazwa) return;
    this.errorMsg = [];

    this.toggleLoading();
    this.postService.findAllPostyByUzytkownik({page: this.page, size: this.size, nazwa: this.nazwa})
    .subscribe({
      next:response=>{
        if (response && response.content) {
          if (response && response.content) {
            if (this.postResponse.content) {
              this.postResponse.content.push(...response.content);
            } else {
              this.postResponse.content = [...response.content];
            }
          }
        }
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        console.error('Error fetching posty:', error);
      },
      complete:()=> this.toggleLoading()
    })
  }

  onScroll= ()=>{
    this.page++;
    this.appendPost();
   }

}
