import { Component, OnInit } from '@angular/core';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { ActivatedRoute, Router } from '@angular/router';
import { findAllRosliny } from '../../../../services/fn/uzytkownik-roslina/find-all-rosliny';
import { PageResponseRoslinaResponse, RoslinaRequest, WlasciwoscWithRelations } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { RoslinaCardComponent } from "../../components/roslina-card/roslina-card.component";
import { WlasciwoscTagComponent } from "../../components/wlasciwosc-tag/wlasciwosc-tag.component";
import { WlasciwoscDropdownComponent } from "../../components/wlasciwosc-dropdown/wlasciwosc-dropdown.component";
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-roslina-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RoslinaCardComponent, WlasciwoscTagComponent, WlasciwoscDropdownComponent],
  templateUrl: './roslina-list.component.html',
  styleUrl: './roslina-list.component.css'
})
export class RoslinaListComponent implements OnInit{
  roslinaResponse: PageResponseRoslinaResponse = {};

  // TODO: Daj sprawdzenia przeciwko from injection
  request: RoslinaRequest = {
    nazwa: '',
    nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: Number.MAX_VALUE,
    wlasciwosci: [

      {
        etykieta: 'Kolor', relacja: 'MA_KOLOR_LISCI', nazwa: 'ciemnozielone'
      },
      {
        etykieta: 'Okres', relacja: 'MA_OKRES_OWOCOWANIA', nazwa: 'październik'
      },
      /*
      {
        etykieta: 'Okres', relacja: 'MA_OKRES_KWITNIENIA', nazwa: 'lipiec'
      },
      */
      {
        etykieta: 'Gleba', relacja: 'MA_GLEBE', nazwa: 'przeciętna ogrodowa'
      },
      {
        etykieta: 'Gleba', relacja: 'MA_GLEBE', nazwa: 'próchniczna'
      }

    ],
  };
  // Na backendzie jest size 10, więc tutaj tylko testuję
  page = 1;
  size = 12;
  pages: number[] = [];
  roslinaCount: number = 0;

  message = '';
  level: 'success' |'error' = 'success';

  constructor(
    private roslinaService: RoslinaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.page = +params['page'] || 1;

      this.request.wysokoscMin = params['wysokoscMin'] || 0;
      this.request.wysokoscMax = params['wysokoscMax'] || Number.MAX_VALUE;
      this.request.nazwa = params['nazwa'] || '';

      this.findAllRosliny();
    });
  }


  findAllRosliny() {
    this.roslinaService.findAllRoslinyWithParameters({
      page: this.page - 1,
      size: this.size,
      body: this.request
    }).subscribe({
        next: (rosliny) => {
          this.roslinaResponse = rosliny;
          this.roslinaCount = rosliny.totalElements as number;
          this.updatePages();
        },
        error: (error) => {
          console.error('Error fetching rosliny:', error);
        }
      });
  }

  onSearch() {
    this.page = 1;
    this.findAllRosliny();
  }

  onWysokoscMinChange(min: number) {
    this.request.wysokoscMin = min;
  }

  onWysokoscMaxChange(max: number) {
    this.request.wysokoscMax = max;
  }

  updatePages() {
    const totalPages = this.roslinaResponse.totalPages as number;
    let startPage = Math.max(1, this.page - 2);
    let endPage = Math.min(totalPages, this.page + 2);

    if (totalPages <= 5) {
      startPage = 1;
      endPage = totalPages;
    } else if (this.page <= 3) {
      endPage = Math.min(totalPages, 5);
    } else if (this.page >= totalPages - 2) {
      startPage = Math.max(1, totalPages - 4);
    }

    this.pages = Array(endPage - startPage + 1).fill(0).map((_, i) => startPage + i);
  }

  goToPage(page: number) {
    this.router.navigate(['/rosliny/page', page], {
      queryParams: {
        page: page,
        wysokoscMin: this.request.wysokoscMin,
        wysokoscMax: this.request.wysokoscMax,
        nazwa: this.request.nazwa
      },
      queryParamsHandling: 'merge'
    });
  }

  goToFirstPage() {
    this.goToPage(1);
  }

  goToNextPage() {
    if (this.page < (this.roslinaResponse.totalPages as number)) {
      this.goToPage(this.page + 1);
    }
  }

  goToPreviousPage() {
    if (this.page > 1) {
      this.goToPage(this.page - 1);
    }
  }

  goToLastPage() {
    const lastPage = this.roslinaResponse.totalPages as number;
    this.goToPage(lastPage);
  }

  get isLastPage() {
    return this.page === (this.roslinaResponse.totalPages as number);
  }

}
