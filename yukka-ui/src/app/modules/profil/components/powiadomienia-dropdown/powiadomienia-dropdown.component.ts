import { Component, OnInit } from '@angular/core';
import { PowiadomienieCardComponent } from "../powiadomienie-card/powiadomienie-card.component";
import { CommonModule } from '@angular/common';
import { PageResponsePowiadomienieResponse } from '../../../../services/models/page-response-powiadomienie-response';
import { PowiadomienieService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { PowiadomienieResponse } from '../../../../services/models';
import { PowiadomieniaSyncService } from '../../services/powiadomieniaSync/powiadomienia-sync.service';
import { TokenService } from '../../../../services/token/token.service';
import { NgbDropdownModule, NgbTooltip, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { LoadingComponent } from "../../../../components/loading/loading.component";

@Component({
  selector: 'app-powiadomienia-dropdown',
  standalone: true,
  imports: [CommonModule, NgbDropdownModule, PowiadomienieCardComponent, InfiniteScrollModule, LoadingComponent, NgbTooltipModule],
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
    private powiadomieniaSyncService: PowiadomieniaSyncService,
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.toggleLoading = () => this.isLoading = !this.isLoading;
  }

  ngOnInit(): void {
    this.updateUnreadCount();
    this.powiadomieniaSyncService.unreadCountUpdated$.subscribe(() => {
      this.updateUnreadCount();
    });

    this.powiadomieniaSyncService.powiadomieniePrzeczytane$.subscribe((pow) => {
      this.updatePowiadomienie(pow);
    });

    this.powiadomieniaSyncService.powiadomienieUsuniete$.subscribe((pow) => {
      if (this.powResponse.content) {
        this.powResponse.content = this.powResponse.content.filter((p: any) => p.id !== pow.id);
      }
    });

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
    console.log('updateUnreadCount: DROPDOWN  - start');
    this.powService.getNieprzeczytaneCountOfUzytkownik().subscribe({
      next: (count) => {
        this.unreadCount = count;
      },
      error: (error) => {
        console.error('Error fetching unread count:', error);
      }
    });
  }

  updatePowiadomienie(pow: PowiadomienieResponse) {
    if (this.powResponse.content) {
      const index = this.powResponse.content.findIndex(p => p.id === pow.id);
      if (index !== -1) {
        this.powResponse.content[index] = pow;
      }
    }
  }

  usunPowiadomienie(pow: PowiadomienieResponse) {
    if (this.powResponse.content) {
      this.powResponse.content = this.powResponse.content.filter(p => p.id !== pow.id);
    }
  }


  goToPowiadomieniaPage() {
    console.log('goToPowiadomieniaPage - start');
    const nazwa = this.tokenService.nazwa;
    this.router.navigate([`/profil/${nazwa}/powiadomienia`]);
  }
}
