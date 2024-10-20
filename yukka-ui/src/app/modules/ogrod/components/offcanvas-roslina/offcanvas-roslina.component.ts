import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { DzialkaRoslinaRequest, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { DzialkaModes } from '../../models/dzialka-modes';
import { DzialkaService } from '../../../../services/services';

@Component({
  selector: 'app-offcanvas-roslina',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './offcanvas-roslina.component.html',
  styleUrl: './offcanvas-roslina.component.css'
})
export class OffcanvasRoslinaComponent {
  @Input() zasadzonaRoslina: ZasadzonaRoslinaResponse | undefined;

  @Input() numerDzialki: number | undefined;
  @Input() mode: string = '';
  @Input() editMode: string = '';
  @Output() roslinaPozycjaEdit = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaKafelkiEdit = new EventEmitter<ZasadzonaRoslinaResponse>();

  @Output() roslinaRemove = new EventEmitter<ZasadzonaRoslinaResponse>();

  @ViewChild('offcanvasBottom', { static: true }) offcanvasBottom!: ElementRef;


  private _roslinaObraz: string | undefined;

  constructor(
    private dzialkaService: DzialkaService
  ) { }

  getRoslina(): RoslinaResponse | undefined {
    return this.zasadzonaRoslina;
  }

  setRoslina(roslina: RoslinaResponse) {
    this.zasadzonaRoslina = roslina;
  }

  getRoslinaObraz(): string | undefined {
    if(this.zasadzonaRoslina?.roslina?.obraz) {
      return 'data:image/jpeg;base64,' + this.zasadzonaRoslina.roslina.obraz;
    }
    return this._roslinaObraz;
  }


  removeRoslinaFromDzialka(): void {
    if(!confirm('Czy na pewno chcesz usunąć roślinę z działki?')) {
      return;
    }
    console.log('getDzialkaByNumer');
    if(!this.zasadzonaRoslina || !this.zasadzonaRoslina.x || !this.zasadzonaRoslina.y) {
      return;
    }
    let deletRequest : DzialkaRoslinaRequest = {
      roslinaId: this.zasadzonaRoslina.roslina?.roslinaId,
      nazwaLacinska: this.zasadzonaRoslina.roslina?.nazwaLacinska,
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
