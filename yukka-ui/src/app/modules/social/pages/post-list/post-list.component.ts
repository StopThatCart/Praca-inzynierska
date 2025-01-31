import { Component } from '@angular/core';
import { PageResponsePostResponse, PostRequest } from '../../../../services/models';
import { PostService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { response } from 'express';
import { PostCardComponent } from "../../components/post-card/post-card.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { AddPostCardComponent } from "../../components/add-post-card/add-post-card.component";
import { LoadingComponent } from "../../../../components/loading/loading.component";
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { SocialNavComponent } from "../../components/social-nav/social-nav.component";
import { SearchComponent } from "../../components/search/search.component";

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [CommonModule, FormsModule, PostCardComponent, InfiniteScrollModule, AddPostCardComponent, LoadingComponent, NgbTooltipModule, SocialNavComponent, SearchComponent],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css'
})
export class PostListComponent {
  postResponse: PageResponsePostResponse = {};
  isLoading = false;
  page = 0;
  size = 5;
  searchText = '';
  pages: number[] = [];
  //postCount: number = 0;

  message = '';

  toggleLoading: () => void;

  constructor(
    private postService: PostService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.toggleLoading = () => this.isLoading = !this.isLoading;
  }

  ngOnInit(): void {
    this.findAllPosty();
  }

  findAllPosty() {
    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 0;

    this.toggleLoading();
    this.postService.findAllPosts({
      page: this.page,
      size: this.size,
      search: this.searchText.trim()
    }).subscribe({
        next: (posty) => {
          this.postResponse = posty;
        },
        error: (error) => {
          console.error('Error fetching posty:', error);
          this.message = 'Wystąpił błąd podczas pobierania postów.';
        },
        complete:()=> this.toggleLoading()
      });
  }

  appendPost= ()=>{
    this.toggleLoading();
    this.postService.findAllPosts({page: this.page, size: this.size, search: this.searchText.trim()})
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
        console.error('Error fetching posty:', error);
        this.message = 'Wystąpił błąd podczas pobierania postów.';
      },
      complete:()=> this.toggleLoading()
    })
  }

  onScroll= ()=>{
    this.page++;
    this.appendPost();
   }

   onSearch = (event: any) => {
    this.page = 0;
    this.searchText = event;
    this.findAllPosty();
  }
}
