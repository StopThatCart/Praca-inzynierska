import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-wlasciwosc-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './wlasciwosc-dropdown.component.html',
  styleUrl: './wlasciwosc-dropdown.component.css'
})
export class WlasciwoscDropdownComponent {
  wysokoscMin: number = 0.0;
  wysokoscMax: number = 100.0;
  wysokoscMinLimit: number = 0.0;
  wysokoscMaxLimit: number = 100.0;

  @Output() wysokoscMinChange = new EventEmitter<number>();
  @Output() wysokoscMaxChange = new EventEmitter<number>();

  onWysokoscMinChange() {
    if (this.wysokoscMin < this.wysokoscMinLimit) {
      this.wysokoscMin = this.wysokoscMinLimit;
    }
    if (this.wysokoscMin > this.wysokoscMax) {
      this.wysokoscMax = this.wysokoscMin;
    }
    this.wysokoscMinChange.emit(this.wysokoscMin);
  }

  onWysokoscMaxChange() {
    if (this.wysokoscMax > this.wysokoscMaxLimit) {
      this.wysokoscMax = this.wysokoscMaxLimit;
    }
    if (this.wysokoscMax < this.wysokoscMin) {
      this.wysokoscMin = this.wysokoscMax;
    }
    this.wysokoscMaxChange.emit(this.wysokoscMax);
  }

}
