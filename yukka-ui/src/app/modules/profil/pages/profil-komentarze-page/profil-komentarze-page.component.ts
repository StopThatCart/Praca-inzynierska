import { Component } from '@angular/core';
import { PageResponseKomentarzResponse } from '../../../../services/models';
import { KomentarzService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { error } from 'console';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { PaginationComponent } from "../../../../components/pagination/pagination.component";
import { KomentarzCardComponent } from "../../../post/components/komentarz-card/komentarz-card.component";
import { CommonModule } from '@angular/common';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { SimpleKomentarzCardComponent } from "../../components/simple-komentarz-card/simple-komentarz-card.component";

@Component({
  selector: 'app-profil-komentarze-page',
  standalone: true,
  imports: [CommonModule, PaginationComponent, KomentarzCardComponent, EdycjaNavComponent, SimpleKomentarzCardComponent],
  templateUrl: './profil-komentarze-page.component.html',
  styleUrl: './profil-komentarze-page.component.css'
})
export class ProfilKomentarzePageComponent {
  komentarze: PageResponseKomentarzResponse = {};
  nazwa : string | undefined;
  isLoading = false;
  page = 1;
  size = 20;
  pages: number[] = [];
  komentarzeCount: number = 0;

  errorMsg: Array<string> = [];
  message = '';

  constructor(
    private komService: KomentarzService,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      if (this.nazwa) {
        this.route.snapshot.data['nazwa'] = this.nazwa;
      }
    });

    this.route.queryParams.subscribe(params => {
      this.page = +params['page'] || 1;
      this.findKomentarzeOfUzytkownik();
    });
  }

  findKomentarzeOfUzytkownik() {
    if(!this.nazwa) return;
    this.errorMsg = [];

    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;
    this.isLoading = true;
    this.komService.findKomentarzeOfUzytkownik({ page: this.page - 1, size: this.size, nazwa: this.nazwa })
      .subscribe({
        next: (response) => {
          this.komentarze = response;
          this.komentarzeCount = response.totalElements as number;
        },
        error: (error) => {
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
          this.isLoading = false;
        },
        complete:()=> this.isLoading = false
      });
  }

  // Paginacja

  goToPage(page: number) {
    console.log('goToPage', page);
    this.router.navigate([`/profil/${this.nazwa}/komentarze`], {
      queryParams: {
        page: page,
        nazwa: this.nazwa
      }
    });
  }

  goToFirstPage() {
    this.goToPage(1);
  }

  goToNextPage() {
    if (this.page < (this.komentarze.totalPages as number)) {
      this.goToPage(this.page + 1);
    }
  }

  goToPreviousPage() {
    if (this.page > 1) {
      this.goToPage(this.page - 1);
    }
  }

  goToLastPage() {
    const lastPage = this.komentarze.totalPages as number;
    this.goToPage(lastPage);
  }

  get isLastPage() {
    return this.page === (this.komentarze.totalPages as number);
  }
}
