import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {
  @Input() page: number = 1;
  @Input() totalPages: number = 1;
  @Output() pageChange = new EventEmitter<number>();

  get isFirstPage(): boolean {
    return this.page === 1;
  }

  get isLastPage(): boolean {
    return this.page === this.totalPages;
  }

  goToPage(page: number) {
    this.pageChange.emit(page);
  }

  goToFirstPage() {
    this.goToPage(1);
  }

  goToPreviousPage() {
    if (!this.isFirstPage) {
      this.goToPage(this.page - 1);
    }
  }

  goToNextPage() {
    if (!this.isLastPage) {
      this.goToPage(this.page + 1);
    }
  }

  goToLastPage() {
    this.goToPage(this.totalPages);
  }

  get pages(): number[] {
    const startPage = Math.max(1, this.page - 2);
    const endPage = Math.min(this.totalPages, this.page + 2);
    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  }
}
