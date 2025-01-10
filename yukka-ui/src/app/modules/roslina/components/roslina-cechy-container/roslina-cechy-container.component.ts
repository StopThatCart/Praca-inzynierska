import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CechaProcessService } from '../../services/cecha-service/cecha.service';
import { RoslinaResponse } from '../../../../services/models';

@Component({
  selector: 'app-roslina-cechy-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './roslina-cechy-container.component.html',
  styleUrl: './roslina-cechy-container.component.css'
})
export class RoslinaCechyContainerComponent implements OnChanges {

  @Input() roslina: RoslinaResponse | undefined;
  roslinaCechy: { name: string, value: string }[] = [];

  constructor(
    private cechaProcessService: CechaProcessService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['roslina'] && this.roslina) {
      this.roslinaCechy = this.cechaProcessService.setRoslinaCechy(this.roslina);
    }
  }

  getRoslinaCechaPary(): { name: string, value: string }[][] {
    return this.cechaProcessService.getRoslinaCechaPary(this.roslinaCechy);
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }

}
