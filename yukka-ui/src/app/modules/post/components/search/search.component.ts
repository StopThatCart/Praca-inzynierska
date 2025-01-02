import { CommonModule } from '@angular/common';
import { Component, Input, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent {
  @Input() searchText = '';
  @Output() searchEvent = new EventEmitter<String>();

  onSearch = () => {
    
    this.searchEvent.emit(this.searchText);
  }
}
