import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { RoslinaResponse } from '../../../../services/models';

@Component({
  selector: 'app-roslina-wlasciwosci-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './roslina-wlasciwosci-container.component.html',
  styleUrl: './roslina-wlasciwosci-container.component.css'
})
export class RoslinaWlasciwosciContainerComponent implements OnChanges {

  @Input() roslina: RoslinaResponse | undefined;
  roslinaWlasciwosci: { name: string, value: string }[] = [];

  constructor(
    private wlasciwoscProcessService: WlasciwoscProcessService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['roslina'] && this.roslina) {
      this.roslinaWlasciwosci = this.wlasciwoscProcessService.setRoslinaWlasciwosci(this.roslina);
    }
  }

  getRoslinaWlasciwoscPary(): { name: string, value: string }[][] {
    return this.wlasciwoscProcessService.getRoslinaWlasciwoscPary(this.roslinaWlasciwosci);
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }

}
