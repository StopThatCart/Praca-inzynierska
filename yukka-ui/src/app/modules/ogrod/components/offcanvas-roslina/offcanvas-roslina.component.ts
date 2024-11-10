import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { BaseDzialkaRequest, DzialkaRoslinaRequest, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { DzialkaModes } from '../../models/dzialka-modes';
import { DzialkaService } from '../../../../services/services';
import { WlasciwoscProcessService } from '../../../roslina/services/wlasciwosc-service/wlasciwosc.service';
import { ColorPickerModule } from 'ngx-color-picker';
import { ModalColorPickComponent } from '../modal-color-pick/modal-color-pick.component';
import { ModalObrazPickComponent } from "../modal-obraz-pick/modal-obraz-pick.component";
import { WyswietlanieRoslinyOpcjeComponent } from "../wyswietlanie-rosliny-opcje/wyswietlanie-rosliny-opcje.component";
import { ModalWyswietlanieRoslinyPickComponent } from "../modal-wyswietlanie-rosliny-pick/modal-wyswietlanie-rosliny-pick.component";

@Component({
  selector: 'app-offcanvas-roslina',
  standalone: true,
  imports: [CommonModule,
    ColorPickerModule,
    ModalColorPickComponent,
    ModalObrazPickComponent,
    WyswietlanieRoslinyOpcjeComponent, ModalWyswietlanieRoslinyPickComponent],
  templateUrl: './offcanvas-roslina.component.html',
  styleUrl: './offcanvas-roslina.component.css'
})
export class OffcanvasRoslinaComponent {

  @Input() numerDzialki: number | undefined;
  @Input() mode: string = '';
  @Input() editMode: string = '';

  @Output() roslinaPozycjaEdit = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaKafelkiEdit = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaKolorChange = new EventEmitter<string>();
  @Output() roslinaSmolChange = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaBaseChange = new EventEmitter<ZasadzonaRoslinaResponse>();

  @Output() roslinaRemove = new EventEmitter<ZasadzonaRoslinaResponse>();

  @ViewChild('offcanvasBottom', { static: true }) offcanvasBottom!: ElementRef;

  @ViewChild(ModalColorPickComponent) colorPickerModal!: ModalColorPickComponent;
  @ViewChild(ModalWyswietlanieRoslinyPickComponent) wyswietlaniePickerModal!: ModalWyswietlanieRoslinyPickComponent;
  @ViewChild(ModalObrazPickComponent) obrazPickerModal!: ModalObrazPickComponent;

  isTextureMode = false;



  roslinaWlasciwosci: { name: string, value: string }[] = [];
  private _roslinaObraz: string | undefined;

  constructor(
    private dzialkaService: DzialkaService,
    private wlasciwoscProcessService: WlasciwoscProcessService
  ) { }

  private _zasadzonaRoslina: ZasadzonaRoslinaResponse | undefined;
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
    let baza = 'data:image/jpeg;base64,';
    if(this.zasadzonaRoslina) {
      if(this.zasadzonaRoslina.obraz) {
        return baza + this.zasadzonaRoslina.obraz
      }else if(this.zasadzonaRoslina.roslina && this.zasadzonaRoslina.roslina.obraz) {
        return baza + this.zasadzonaRoslina.roslina.obraz
      }
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
    let deletRequest = this.makeBaseDzialkaRequest();

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

  openObrazPickerModal(isTextureMode: boolean = false) {
    this.isTextureMode = isTextureMode;
    this.obrazPickerModal.openPickerModal();

  }

  openColorPickerModal() {
    this.colorPickerModal.openPickerModal();
  }


  confirmColorChange(newColor: string) {
    console.log('confirmColorChange', newColor);
    if(!this.zasadzonaRoslina) return;
    this.zasadzonaRoslina!.kolor = newColor;

    let request: DzialkaRoslinaRequest = this.makeDzialkaRoslinaRequest();
    request.kolor = newColor;

    this.dzialkaService .updateRoslinaKolorInDzialka( { body: request }).subscribe({
      next: () => {
        console.log('kolorowanie yeeeeeey');
        this.roslinaKolorChange.emit(newColor);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  openWyswietlaniePickerModal() {
    this.wyswietlaniePickerModal.openPickerModal();
  }

  confirmWyswietlanieChange(newWyswietlanie: string) {
    console.log('confirmWyswietlanieChange', newWyswietlanie);
    if(!this.zasadzonaRoslina) return;

    let request: DzialkaRoslinaRequest = this.makeDzialkaRoslinaRequest();
    request.wyswietlanie = newWyswietlanie;

    this.dzialkaService .updateRoslinaWyswietlanieInDzialka( { body: request }).subscribe({
      next: () => {
        console.log('wyswietlanie yeeeeeey');
        this.zasadzonaRoslina!.wyswietlanie = newWyswietlanie;
        this.roslinaSmolChange.emit(this.zasadzonaRoslina);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  confirmObrazChange(obraz: any) {
    console.log('confirmObrazChange', obraz);
    if(!this.zasadzonaRoslina) return;
    if(obraz === null) {
      let deletRequest = this.makeBaseDzialkaRequest();
      if(this.isTextureMode) {
        this.dzialkaService.deleteRoslinaTeksturaFromDzialka( { body: deletRequest }).subscribe({
          next: () => {
            console.log('usunięto teksturę');
            this.roslinaSmolChange.emit(this.zasadzonaRoslina);
          },
          error: (err) => {
            console.error(err);
          }
        });
      } else {
        this.dzialkaService.deleteRoslinaObrazFromDzialka( { body: deletRequest }).subscribe({
          next: () => {
            console.log('usunięto obraz');
            this.zasadzonaRoslina!.obraz = this.zasadzonaRoslina?.roslina?.obraz;
            console.log(this.zasadzonaRoslina);

            this.roslinaSmolChange.emit(this.zasadzonaRoslina);
          },
          error: (err) => {
            console.error(err);
          }
        });
      }
    } else {
      let request = this.makeDzialkaRoslinaRequest();

      let teksturka : any = null;
      let obrazek : any = null;
      this.isTextureMode ? (teksturka = obraz) : (obrazek = obraz);

      console.log('W trybie tekstury: ' + this.isTextureMode);
      console.log(teksturka == null ? 'null' : 'nie null');
      console.log(obrazek == null ? 'null' : 'nie null');

      this.dzialkaService.updateRoslinaObrazInDzialka( { body: {request : request, obraz: obrazek, tekstura: teksturka } }).subscribe({
        next: (obrazek) => {
          console.log('zaktualizowano obraz');
          console.log('W trybie tekstury: ' + this.isTextureMode);
          console.log(obrazek);
          //if(!this.isTextureMode) this.zasadzonaRoslina!.obraz = obrazek.content;
          this.roslinaSmolChange.emit(this.zasadzonaRoslina);
        },
        error: (err) => {
          console.error(err);
        }
      });
    }
  }


  private makeDzialkaRoslinaRequest(): DzialkaRoslinaRequest {
    return {
      numerDzialki: this.numerDzialki!,
      x: this.zasadzonaRoslina!.x!,
      y: this.zasadzonaRoslina!.y!,
      pozycje: [{ x: this.zasadzonaRoslina!.x!, y: this.zasadzonaRoslina!.y! }],
      wyswietlanie: this.zasadzonaRoslina!.wyswietlanie!,
      kolor: this.zasadzonaRoslina!.kolor!
    };
  }

  private makeBaseDzialkaRequest(): BaseDzialkaRequest {
    return {
      numerDzialki: this.numerDzialki!,
      pozycje: [{ x: this.zasadzonaRoslina!.x!, y: this.zasadzonaRoslina!.y! }],
      x: this.zasadzonaRoslina!.x!,
      y: this.zasadzonaRoslina!.y!
    };
  }




}
