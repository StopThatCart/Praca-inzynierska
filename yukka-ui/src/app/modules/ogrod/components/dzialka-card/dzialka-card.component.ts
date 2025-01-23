import { Component, Input } from '@angular/core';
import { DzialkaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dzialka-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dzialka-card.component.html',
  styleUrl: './dzialka-card.component.css'
})
export class DzialkaCardComponent {
  @Input() dzialka : DzialkaResponse = {};


  hasRosliny(): boolean {
    return this.dzialka && typeof this.dzialka.liczbaRoslin === 'number' && this.dzialka.liczbaRoslin > 0;
  }
}
