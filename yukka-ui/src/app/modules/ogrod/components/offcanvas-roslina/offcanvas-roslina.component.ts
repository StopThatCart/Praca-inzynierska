import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { BaseDzialkaRequest, DzialkaRoslinaRequest, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { DzialkaModes } from '../../models/dzialka-modes';
import { DzialkaService } from '../../../../services/services';
import { WlasciwoscProcessService } from '../../../roslina/services/wlasciwosc-service/wlasciwosc.service';
import { ColorPickerModule } from 'ngx-color-picker';

@Component({
  selector: 'app-offcanvas-roslina',
  standalone: true,
  imports: [CommonModule, ColorPickerModule],
  templateUrl: './offcanvas-roslina.component.html',
  styleUrl: './offcanvas-roslina.component.css'
})
export class OffcanvasRoslinaComponent {

  @Input() numerDzialki: number | undefined;
  @Input() mode: string = '';
  @Input() editMode: string = '';
  @Output() roslinaPozycjaEdit = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaKafelkiEdit = new EventEmitter<ZasadzonaRoslinaResponse>();

  @Output() roslinaRemove = new EventEmitter<ZasadzonaRoslinaResponse>();

  @ViewChild('offcanvasBottom', { static: true }) offcanvasBottom!: ElementRef;


  roslinaWlasciwosci: { name: string, value: string }[] = [];
  private _roslinaObraz: string | undefined;

  constructor(
    private dzialkaService: DzialkaService,
    private wlasciwoscProcessService: WlasciwoscProcessService
  ) { }

  private _zasadzonaRoslina: ZasadzonaRoslinaResponse | undefined;
  // @Input() zasadzonaRoslina: ZasadzonaRoslinaResponse | undefined;
   @Input()
   set zasadzonaRoslina(roslina: ZasadzonaRoslinaResponse | undefined) {
     this._zasadzonaRoslina = roslina;
     if (roslina && roslina.roslina) {
       this.roslinaWlasciwosci = this.wlasciwoscProcessService.setRoslinaWlasciwosci(roslina.roslina);
     } else {
       this.roslinaWlasciwosci = [];
     }
   }

  get zasadzonaRoslina(): ZasadzonaRoslinaResponse | undefined {
    return this._zasadzonaRoslina;
  }

  getRoslinaObraz(): string | undefined {
    if(this.zasadzonaRoslina?.roslina?.obraz) {
      return 'data:image/jpeg;base64,' + this.zasadzonaRoslina.roslina.obraz;
    }
    return this._roslinaObraz;
  }

  getRoslinaWlasciwoscPary(): { name: string, value: string }[][] {
    if(!this.roslinaWlasciwosci && this.zasadzonaRoslina?.roslina) {
      this.roslinaWlasciwosci = this.wlasciwoscProcessService.setRoslinaWlasciwosci(this.zasadzonaRoslina?.roslina);
    }
    return this.wlasciwoscProcessService.getRoslinaWlasciwoscPary(this.roslinaWlasciwosci);
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }


  removeRoslinaFromDzialka(): void {
    if(!confirm('Czy na pewno chcesz usunąć roślinę z działki?')) {
      return;
    }
    console.log('getDzialkaByNumer');
    if(!this.zasadzonaRoslina || this.zasadzonaRoslina.x == undefined || this.zasadzonaRoslina.y == undefined) {
      console.error('Nie można usunąć rośliny z działki, brak pozycji');
      console.log("sprawdzanie")
      console.log(this.zasadzonaRoslina)
      console.log(this.zasadzonaRoslina?.x)
      console.log(this.zasadzonaRoslina?.y)
      return;
    }
    let deletRequest : BaseDzialkaRequest = {
      numerDzialki: this.numerDzialki!,
      pozycje: [{ x: this.zasadzonaRoslina.x, y: this.zasadzonaRoslina.y }],
      x: this.zasadzonaRoslina.x,
      y: this.zasadzonaRoslina.y
    };

    this.dzialkaService.deleteRoslinaFromDzialka( { body: deletRequest }).subscribe({
      next: () => {
        console.log('roslinaRemove');

        this.roslinaRemove.emit(this.zasadzonaRoslina);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  sendRoslinaPozycjaEditEvent() {
    if(this.editMode && this.editMode !== DzialkaModes.Edit) {
      this.roslinaPozycjaEdit.emit(this.zasadzonaRoslina);
    }
  }

  sendRoslinaKafelkiEditEvent() {
    if(this.editMode && this.editMode !== DzialkaModes.Edit) {
      this.roslinaKafelkiEdit.emit(this.zasadzonaRoslina);
    }
  }



}
