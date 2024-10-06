import { Component, OnInit } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { PowiadomienieCardComponent } from "../../components/powiadomienie-card/powiadomienie-card.component";
import { CommonModule } from '@angular/common';
import { PageResponsePowiadomienieResponse, PowiadomienieResponse } from '../../../../services/models';
import { PowiadomienieService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { PaginationComponent } from "../../../../components/pagination/pagination.component";
import { PowiadomieniaSyncService } from '../../services/powiadomieniaSync/powiadomienia-sync.service';

@Component({
  selector: 'app-powiadomienia-page',
  standalone: true,
  imports: [CommonModule, EdycjaNavComponent, PowiadomienieCardComponent, PaginationComponent],
  templateUrl: './powiadomienia-page.component.html',
  styleUrl: './powiadomienia-page.component.css'
})
export class PowiadomieniaPageComponent implements OnInit {
  powiadomienia: PageResponsePowiadomienieResponse = {};
  nazwa : string | undefined;
  isLoading = false;
  page = 1;
  size = 5;
  pages: number[] = [];
  powiadomieniaCount: number = 0;

  message = '';

  constructor(
    private powService: PowiadomienieService,
    private powiadomieniaSyncService: PowiadomieniaSyncService,
    private router: Router,
    private route: ActivatedRoute
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
     // this.nazwa = params['nazwa'];
      this.findAllPowiadomienia();
    });

    this.powiadomieniaSyncService.powiadomienieUsuniete$.subscribe((pow) => {
      if (this.powiadomienia.content) {
        this.findAllPowiadomienia();
        this.powiadomienia.content = this.powiadomienia.content.filter((p: any) => p.id !== pow.id);
      }
    });

    this.powiadomieniaSyncService.powiadomieniePrzeczytane$.subscribe((pow) => {
      this.updatePowiadomienie(pow);
    });

   // console.log(this.powResponse);
  }

  findAllPowiadomienia() {
    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;

    this.isLoading = true;
    this.powService.getPowiadomienia({ page: this.page - 1, size: this.size })
      .subscribe({
        next: (powiadomienia) => {
          this.powiadomienia = powiadomienia;
          this.powiadomieniaCount = powiadomienia.totalElements as number;
        },
        error: (error) => {
          console.error('Error fetching powiadomienia:', error);
          this.message = 'Wystąpił błąd podczas pobierania powiadomień.';
        },
        complete:()=> this.isLoading = false
      });
  }

  updateUnreadCount() {
    this.powiadomieniaSyncService.notifyUnreadCountUpdated();
  }

  updatePowiadomienie(pow: PowiadomienieResponse) {
    if (this.powiadomienia.content) {
      const index = this.powiadomienia.content.findIndex(p => p.id === pow.id);
      if (index !== -1) {
        this.powiadomienia.content[index] = pow;
      }
    }
  }

  onPowiadomieniePrzeczytane(pow: PowiadomienieResponse) {
    this.powiadomieniaSyncService.notifyUnreadCountUpdated();
    this.powiadomieniaSyncService.notifyPowiadomieniePrzeczytane(pow);
  }

  usunPowiadomienie(pow: PowiadomienieResponse) {
    if (this.powiadomienia.content) {
      this.powiadomienia.content = this.powiadomienia.content.filter(p => p.id !== pow.id);
    }
  }

  // Paginacja

  goToPage(page: number) {
    console.log('goToPage', page);
    this.router.navigate([`/profil/${this.nazwa}/powiadomienia`], {
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
    if (this.page < (this.powiadomienia.totalPages as number)) {
      this.goToPage(this.page + 1);
    }
  }

  goToPreviousPage() {
    if (this.page > 1) {
      this.goToPage(this.page - 1);
    }
  }

  goToLastPage() {
    const lastPage = this.powiadomienia.totalPages as number;
    this.goToPage(lastPage);
  }

  get isLastPage() {
    return this.page === (this.powiadomienia.totalPages as number);
  }

}
