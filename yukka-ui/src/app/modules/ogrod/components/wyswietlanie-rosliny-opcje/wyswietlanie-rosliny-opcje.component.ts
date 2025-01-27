import { Component, Input, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WyswietlanieRosliny } from '../../../social/models/WyswietlanieRosliny';
import { ZasadzonaRoslinaResponse } from '../../../../services/models';

@Component({
  selector: 'app-wyswietlanie-rosliny-opcje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './wyswietlanie-rosliny-opcje.component.html',
  styleUrl: './wyswietlanie-rosliny-opcje.component.css'
})
export class WyswietlanieRoslinyOpcjeComponent {

  @Input() wyswietlanie : string | undefined;
  @Input() selectedRoslina: ZasadzonaRoslinaResponse | undefined;

  wyswietlanieOpcje = WyswietlanieRosliny;

  @Output() wyswietlanieChange = new EventEmitter<String>();

  onWyswietlanieChange(event: Event): void {
    this.wyswietlanieChange.emit(this.wyswietlanie);
  }

}
