import { Component } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { RozmowaCardComponent } from "../../components/rozmowa-card/rozmowa-card.component";
import { PageResponseRozmowaPrywatnaResponse, UzytkownikResponse } from '../../../../services/models';
import { RozmowaPrywatnaService, UzytkownikService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { getBlokowaniAndBlokujacy } from '../../../../services/fn/uzytkownik/get-blokowani-and-blokujacy';
import { LoadingComponent } from "../../../../components/loading/loading.component";

@Component({
  selector: 'app-rozmowy-list',
  standalone: true,
  imports: [CommonModule, InfiniteScrollModule, EdycjaNavComponent, RozmowaCardComponent, LoadingComponent],
  templateUrl: './rozmowy-list.component.html',
  styleUrl: './rozmowy-list.component.css'
})
export class RozmowyListComponent {
  rozResponse: PageResponseRozmowaPrywatnaResponse = {};
  isLoading = false;
  page = 0;
  size = 5;
  pages: number[] = [];

  currentUzyt: UzytkownikResponse = {};
  blokowaniUzytkownicy: UzytkownikResponse[] = [];
  blokujacyUzytkownicy: UzytkownikResponse[] = [];

  toggleLoading: () => void;

  constructor(
    private rozmowaService: RozmowaPrywatnaService,
    private uzytService: UzytkownikService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.toggleLoading = () => this.isLoading = !this.isLoading;
  }

  ngOnInit(): void {
    this.getBlokowaniAndBlokujacy();
    this.findRozmowyOfUzytkownik();
  }

  handleReject(nazwa: string) {
    if (this.rozResponse.content) {
      this.rozResponse.content = this.rozResponse.content.filter(rozmowa =>
        !(rozmowa.uzytkownicy ?? []).some(user => user.nazwa === nazwa)
      );
    }
  }

  getBlokowaniAndBlokujacy() {
    this.uzytService.getBlokowaniAndBlokujacy().subscribe({
      next: (response) => {
        this.currentUzyt = response;
        this.blokowaniUzytkownicy = response.blokowaniUzytkownicy ?? [];
        this.blokujacyUzytkownicy = response.blokujacyUzytkownicy ?? [];
      },
      error: (error) => {
        console.error('Error fetching blokowani and blokujacy:', error);
      }
    });
  }

  findRozmowyOfUzytkownik() {
    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;

    this.toggleLoading();
    this.rozmowaService.findRozmowyPrywatneOfUzytkownik({
      page: this.page,
      size: this.size
    }).subscribe({
        next: (rozmowy) => {
          this.rozResponse = rozmowy;
        },
        error: (error) => {
          console.error('Error fetching rozmowy:', error);
        },
        complete:()=> this.toggleLoading()

      });
  }

  isBlokowany(uzytkownicy: Array<UzytkownikResponse> | undefined): boolean {
    if (uzytkownicy) {
      return uzytkownicy.some(uzytkownik =>
        this.blokowaniUzytkownicy?.some(blokowany => blokowany.nazwa === uzytkownik.nazwa)
      );
    }
    return false;
  }

  isBlokujacy(uzytkownicy: Array<UzytkownikResponse> | undefined): boolean {
    if (uzytkownicy) {
      return uzytkownicy.some(uzytkownik =>
        this.blokujacyUzytkownicy?.some(blokowany => blokowany.nazwa === uzytkownik.nazwa)
      );
    }
    return false;
  }

  appendPost= ()=>{
    this.toggleLoading();
    this.rozmowaService.findRozmowyPrywatneOfUzytkownik({page: this.page, size: this.size})
    .subscribe({
      next:response=>{
        if (response && response.content) {
          if (response && response.content) {
            if (this.rozResponse.content) {
              this.rozResponse.content.push(...response.content);
            } else {
              this.rozResponse.content = [...response.content];
            }
          }
        }
      },
      error: (error) => {
        console.error('Error fetching rozmowy:', error);
      },
      complete:()=> this.toggleLoading()
    })
  }

  onScroll= ()=>{
    this.page++;
    this.appendPost();
   }

}
