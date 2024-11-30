import { Component, OnInit, ViewChild } from '@angular/core';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PageResponseRoslinaResponse, RoslinaRequest, WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { RoslinaCardComponent } from "../../components/roslina-card/roslina-card.component";
import { WlasciwoscTagComponent } from "../../components/wlasciwosc-tag/wlasciwosc-tag.component";
import { WlasciwoscDropdownComponent } from "../../components/wlasciwosc-dropdown/wlasciwosc-dropdown.component";
import { FormsModule } from '@angular/forms';
import { SpinnerComponent } from "../../../../services/LoaderSpinner/spinner/spinner.component";
import { Convert } from '../../../../services/converts/wlasciwosc-with-relations-convert';
import { PaginationComponent } from "../../../../components/pagination/pagination.component";
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { WysokoscInputComponent } from "../../components/wysokosc-input/wysokosc-input.component";
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-roslina-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RoslinaCardComponent, WlasciwoscTagComponent, WlasciwoscDropdownComponent, SpinnerComponent, PaginationComponent, WysokoscInputComponent],
  templateUrl: './roslina-list.component.html',
  styleUrl: './roslina-list.component.css'
})
export class RoslinaListComponent implements OnInit{
  canAddRoslina: boolean = false;

  roslinaResponse: PageResponseRoslinaResponse = {};
  wlasciwosciResponse: WlasciwoscResponse[] = [];
  isLoading = false;
  message = '';

  request: RoslinaRequest = {
    nazwa: '',
    nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: 100,
    wlasciwosci: [
/*
      {
        etykieta: 'Kolor', relacja: 'MA_KOLOR_LISCI', nazwa: 'ciemnozielone'
      },
      {
        etykieta: 'Okres', relacja: 'MA_OKRES_OWOCOWANIA', nazwa: 'październik'
      },

      {
        etykieta: 'Okres', relacja: 'MA_OKRES_KWITNIENIA', nazwa: 'lipiec'
      },

      {
        etykieta: 'Gleba', relacja: 'MA_GLEBE', nazwa: 'przeciętna ogrodowa'
      },
      {
        etykieta: 'Gleba', relacja: 'MA_GLEBE', nazwa: 'próchniczna'
      }
*/
    ] as WlasciwoscWithRelations[],
  };
  // Na backendzie jest size 10, więc tutaj tylko testuję
  page = 1;
  size = 12;
  roslinaCount: number = 0;

  @ViewChild(WlasciwoscTagComponent) wlasciwoscTagComponent!: WlasciwoscTagComponent;

  constructor(
    private roslinaService: RoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.checkRoles();

    this.route.queryParams.subscribe(params => {
      this.page = +params['page'] || 1;

      this.request.wysokoscMin = params['wysokoscMin'] || 0;
      this.request.wysokoscMax = params['wysokoscMax'] || 100;
      this.request.nazwa = params['nazwa'] || '';
      this.request.nazwaLacinska = params['nazwaLacinska'] || '';

      // Uwaga: uważaj na api-gen, bo trzeba ręcznie tworzyć konwersję na JSON
      // Tutaj link dla przyszłego mnie. https://app.quicktype.io/
      let wlasciwosci2 = params['wlasciwosci'] ? JSON.parse(params['wlasciwosci']) : [];
     // console.log("Właściwości2: " + wlasciwosci2);

      this.request.wlasciwosci = Convert.toWlasciwoscWithRelationsArray(JSON.stringify(wlasciwosci2));

      this.findAllRosliny();
    });
  }

  private checkRoles() {
    this.canAddRoslina = this.tokenService.isAdmin() || this.tokenService.isPracownik();
  }

  goToAddRoslina(doKatalogu: boolean) {
    if(this.canAddRoslina) {
      this.router.navigate(['rosliny/dodaj', doKatalogu]);
    }
  }

  /*
  fetchWlasciwosciFromString(wlasciwosciString: string): WlasciwoscWithRelations[] {
    let lel: WlasciwoscWithRelations[] = [];
    let items = wlasciwosciString.substring(1, wlasciwosciString.length - 1).split('},{');
    items = items.map((item: string) => (item[0] !== '{' ? '{' + item : item) + (item[item.length - 1] !== '}' ? '}' : ''));
    for (let item of items) {
      console.log("Item: " + item);
      let w = { etykieta: '', relacja: '', nazwa: '' } as WlasciwoscWithRelations;

      let etykietaMatch = item.match(/"etykieta":"(.*?)"/);
      let relacjaMatch = item.match(/"relacja":"(.*?)"/);
      let nazwaMatch = item.match(/"nazwa":"(.*?)"/);

      if (etykietaMatch) w.etykieta = etykietaMatch[1];
      if (relacjaMatch) w.relacja = relacjaMatch[1];
      if (nazwaMatch) w.nazwa = nazwaMatch[1];

      lel.push(w);
    }
    return lel;
  }
    */


  findAllRosliny() {
   // console.log('Request:', this.request);
    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;

    this.isLoading = true;
    this.roslinaService.findAllRoslinyWithParameters({
      page: this.page - 1,
      size: this.size,
      body: this.request
    }).subscribe({
        next: (rosliny) => {
          this.roslinaResponse = rosliny;
          this.roslinaCount = rosliny.totalElements as number;
        },
        error: (error) => {
          console.error('Error fetching rosliny:', error);
          this.message = 'Wystąpił błąd podczas pobierania roślin.';
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        }
      });

    this.roslinaService.getWlasciwosciWithRelations()
    .subscribe({
      next: (wlasciwosci) => {
        this.wlasciwosciResponse = this.wlasciwoscProcessService.processWlasciwosciResponse(wlasciwosci);
      },
      error: (error) => {
        console.error('Error fetching wlasciwosci:', error);
      }
    });
  }

  onSearch() {
   // console.log('Search:', this.request);
    this.goToFirstPage();
   // this.findAllRosliny();
  }

  onWysokoscMinChange(min: number) {
    this.request.wysokoscMin = min;
  }

  onWysokoscMaxChange(max: number) {
    this.request.wysokoscMax = max;
  }

  onWlasciwoscToggled(wlasciwosci: WlasciwoscWithRelations[]): void {
    console.log('Wlasciwosci toggled:', wlasciwosci);
    this.request.wlasciwosci = wlasciwosci;
    this.wlasciwoscTagComponent.updateSortedWlasciwosci(wlasciwosci);
  }

  onWlasciwoscRemoved(index: number): void {
    console.log('Removing wlasciwosc at index:', index);

    this.request.wlasciwosci.splice(index, 1);
    console.log('Request after removing:', this.request);
  }

  // Paginacja

  goToPage(page: number) {
    const wlasciwosciJson = Convert.wlasciwoscWithRelationsArrayToJson(this.request.wlasciwosci);

    this.router.navigate(['/rosliny'], {
      queryParams: {
        page: page,
        wysokoscMin: this.request.wysokoscMin,
        wysokoscMax: this.request.wysokoscMax,
        nazwa: this.request.nazwa,
        nazwaLacinska: this.request.nazwaLacinska,
        wlasciwosci: wlasciwosciJson
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
