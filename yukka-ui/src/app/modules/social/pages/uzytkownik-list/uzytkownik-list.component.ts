import { Component } from '@angular/core';
import { PageResponseUzytkownikResponse } from '../../../../services/models';
import { UzytkownikService } from '../../../../services/services';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { LoadingComponent } from '../../../../components/loading/loading.component';
import { UzytkownikCardComponent } from "../../components/uzytkownik-card/uzytkownik-card.component";
import { SocialNavComponent } from "../../components/social-nav/social-nav.component";
import { SearchComponent } from "../../components/search/search.component";
import e from 'express';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-uzytkownik-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, InfiniteScrollModule, LoadingComponent, NgbTooltipModule, UzytkownikCardComponent, SocialNavComponent, SearchComponent],
  templateUrl: './uzytkownik-list.component.html',
  styleUrl: './uzytkownik-list.component.css'
})
export class UzytkownikListComponent {
  uzytResponse: PageResponseUzytkownikResponse = {};
  isLoading = false;
  page = 0;
  size = 5;
  searchText = '';
  pages: number[] = [];
  //postCount: number = 0;

  message = '';

  toggleLoading: () => void;

  constructor(
    private uzytService: UzytkownikService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) {
    this.toggleLoading = () => this.isLoading = !this.isLoading;
  }

  ngOnInit(): void {
    this.findAllUzytkownicy();
    console.log(this.uzytResponse);
  }

  isAdmin(): boolean {
    return this.tokenService.isAdmin();
  }

  findAllUzytkownicy() {
    console.log('findAllUzytkownicy');

    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 0;

    this.toggleLoading();
    this.uzytService.findAllUzytkownicy({
      page: this.page,
      size: this.size,
      szukaj: this.searchText.trim()
    }).subscribe({
        next: (posty) => {
          this.uzytResponse = posty;
        },
        error: (error) => {
          console.error('Error fetching uzytkownicy:', error);
          this.message = 'Wystąpił błąd podczas pobierania użytkowników.';
        },
        complete:()=> this.toggleLoading()

      });
  }

  appendUzytkownik= ()=>{
    this.toggleLoading();
    this.uzytService.findAllUzytkownicy({page: this.page, size: this.size, szukaj: this.searchText.trim()})
    .subscribe({
      next:response=>{
        if (response && response.content) {
          if (response && response.content) {
            if (this.uzytResponse.content) {
              this.uzytResponse.content.push(...response.content);
            } else {
              this.uzytResponse.content = [...response.content];
            }
          }
        }
      },
      error: (error) => {
        console.error('Error fetching uzytkownicy', error);
        this.message = 'Wystąpił błąd podczas pobierania użytkowników.';
      },
      complete:()=> this.toggleLoading()
    })
  }

  onScroll= ()=>{
    this.page++;
    this.appendUzytkownik();
  }

  onSearch = (event: any) => {
    this.page = 0;
    this.searchText = event;
    this.findAllUzytkownicy();
  }
}
