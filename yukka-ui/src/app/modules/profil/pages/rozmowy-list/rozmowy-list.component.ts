import { Component } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { RozmowaCardComponent } from "../../components/rozmowa-card/rozmowa-card.component";
import { PageResponseRozmowaPrywatnaResponse } from '../../../../services/models';
import { RozmowaPrywatnaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

@Component({
  selector: 'app-rozmowy-list',
  standalone: true,
  imports: [CommonModule, InfiniteScrollModule, EdycjaNavComponent, RozmowaCardComponent],
  templateUrl: './rozmowy-list.component.html',
  styleUrl: './rozmowy-list.component.css'
})
export class RozmowyListComponent {
  rozResponse: PageResponseRozmowaPrywatnaResponse = {};
  isLoading = false;
  page = 0;
  size = 5;
  pages: number[] = [];

  toggleLoading: () => void;

  constructor(
    private rozmowaService: RozmowaPrywatnaService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.toggleLoading = () => this.isLoading = !this.isLoading;
  }

  ngOnInit(): void {
    this.findRozmowyOfUzytkownik();
    console.log(this.rozResponse);
  }


  findRozmowyOfUzytkownik() {
    console.log('findRozmowyOfUzytkownik');

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
