import { Component, OnInit } from '@angular/core';
import { PowiadomienieCardComponent } from "../powiadomienie-card/powiadomienie-card.component";
import { CommonModule } from '@angular/common';
import { PageResponsePowiadomienieResponse } from '../../../../services/models/page-response-powiadomienie-response';
import { PowiadomienieService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { PowiadomienieResponse } from '../../../../services/models';

@Component({
  selector: 'app-powiadomienia-dropdown',
  standalone: true,
  imports: [CommonModule, PowiadomienieCardComponent, InfiniteScrollModule],
  templateUrl: './powiadomienia-dropdown.component.html',
  styleUrl: './powiadomienia-dropdown.component.css'
})
export class PowiadomieniaDropdownComponent implements OnInit {
  powResponse: PageResponsePowiadomienieResponse = {};
  isLoading = false;
  page = 0;
  size = 5;
  pages: number[] = [];
  unreadCount = 0;

  message = '';
  isDropdownOpen = false;

  toggleLoading: () => void;

  constructor(
    private powService: PowiadomienieService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.toggleLoading = () => this.isLoading = !this.isLoading;
  }

  ngOnInit(): void {
    this.updateUnreadCount();
   // this.findAllPowiadomienia();
   // console.log(this.powResponse);
  }

  loadPowiadomienia() {
    if (this.isDropdownOpen && !this.powResponse.content) {
      this.findAllPowiadomienia();
    }
  }

  findAllPowiadomienia() {
    console.log('findAllPowiadomienia');

    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;

    this.toggleLoading();
    this.powService.getPowiadomienia({ page: this.page, size: this.size })
      .subscribe({
        next: (powiadomienia) => {
          this.powResponse = powiadomienia;
          this.updateUnreadCount();
          console.log(this.powResponse);
        },
        error: (error) => {
          console.error('Error fetching powiadomienia:', error);
          this.message = 'Wystąpił błąd podczas pobierania powiadomień.';
        },
        complete:()=> this.toggleLoading()
      });
  }

  appendPow= ()=>{
    console.log('appendPow: ' + this.page);
    this.toggleLoading();
    this.powService.getPowiadomienia({page: this.page, size: this.size})
    .subscribe({
      next:response=>{
        if (response && response.content) {
          if (response && response.content) {
            if (this.powResponse.content) {
              this.powResponse.content.push(...response.content);
            } else {
              this.powResponse.content = [...response.content];
            }
            this.updateUnreadCount();
          }
        }
      },
      error: (error) => {
        console.error('Error fetching powiadomienia:', error);
        this.message = 'Wystąpił błąd podczas pobierania powiadomień.';
      },
      complete:()=> this.toggleLoading()
    })
  }

  onScroll= ()=>{
    this.page++;
    this.appendPow();
   }

   toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
    if (this.isDropdownOpen) {
      this.loadPowiadomienia();
    }
  }

  updateUnreadCount() {
    this.powService.getNieprzeczytaneCountOfUzytkownik().subscribe({
      next: (count) => {
        this.unreadCount = count;
      },
      error: (error) => {
        console.error('Error fetching unread count:', error);
      }
    });
  }

  usunPowiadomienie(pow: PowiadomienieResponse) {
    if (this.powResponse.content) {
      this.powResponse.content = this.powResponse.content.filter(p => p.id !== pow.id);
    }
  }
}
