import { Component, OnInit } from '@angular/core';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { ActivatedRoute, Router } from '@angular/router';
import { findAllRosliny } from '../../../../services/fn/uzytkownik-roslina/find-all-rosliny';
import { PageResponseRoslinaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { RoslinaCardComponent } from "../../components/roslina-card/roslina-card.component";


@Component({
  selector: 'app-roslina-list',
  standalone: true,
  imports: [CommonModule, RoslinaCardComponent],
  templateUrl: './roslina-list.component.html',
  styleUrl: './roslina-list.component.css'
})
export class RoslinaListComponent implements OnInit{
  roslinaResponse: PageResponseRoslinaResponse = {};
  // Na backendzie jest size 10, więc tutaj tylko testuję
  page = 1;
  size = 12;
  pages: number[] = [];
  message = '';
  level: 'success' |'error' = 'success';

  constructor(
    private roslinaService: RoslinaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.page = +params['page'] || 1;
      this.findAllRosliny();
    });
  }

  findAllRosliny() {
    //console.log('findAllRosliny');
    this.roslinaService.findAllRosliny1({ page: this.page - 1, size: this.size })
      .subscribe({
        next: (rosliny) => {
          this.roslinaResponse = rosliny;
          this.updatePages();
        }
      });
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

  gotToPage(page: number) {
    this.router.navigate(['/rosliny/page', page]);
  }

  goToFirstPage() {
    this.router.navigate(['/rosliny/page', 1]);
  }

  goToPreviousPage() {
    if (this.page > 1) {
      this.router.navigate(['/rosliny/page', this.page - 1]);
    }
  }

  goToLastPage() {
    const lastPage = this.roslinaResponse.totalPages as number;
    this.router.navigate(['/rosliny/page', lastPage]);
  }

  goToNextPage() {
    if (this.page < (this.roslinaResponse.totalPages as number)) {
      this.router.navigate(['/rosliny/page', this.page + 1]);
    }
  }

  get isLastPage() {
    return this.page === (this.roslinaResponse.totalPages as number - 1);
  }

  private isLocalStorageAvailable(): boolean {
    return typeof localStorage !== 'undefined';
  }



}
