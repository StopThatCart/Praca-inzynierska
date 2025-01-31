import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { BaseDzialkaRequest, DzialkaRoslinaRequest, RoslinaResponse, UzytkownikResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { DzialkaModes } from '../../models/dzialka-modes';
import { DzialkaService } from '../../../../services/services';
import { CechaProcessService } from '../../../roslina/services/cecha-service/cecha.service';
import { ColorPickerModule } from 'ngx-color-picker';
import { ModalColorPickComponent } from '../modal-color-pick/modal-color-pick.component';
import { ModalObrazPickComponent } from "../modal-obraz-pick/modal-obraz-pick.component";
import { ModalWyswietlanieRoslinyPickComponent } from "../modal-wyswietlanie-rosliny-pick/modal-wyswietlanie-rosliny-pick.component";
import { ModalNotatkaPickComponent } from "../modal-notatka-pick/modal-notatka-pick.component";
import { NgbCollapseModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { TokenService } from '../../../../services/token/token.service';
import { WyswietlanieRosliny } from '../../../social/models/WyswietlanieRosliny';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { RoslinaCechyContainerComponent } from "../../../roslina/components/roslina-cechy-container/roslina-cechy-container.component";

@Component({
  selector: 'app-offcanvas-roslina',
  standalone: true,
  imports: [CommonModule,
    RouterModule,
    ColorPickerModule,
    NgbCollapseModule,
    NgbTooltipModule,
    ModalColorPickComponent,
    ModalObrazPickComponent,
    ModalWyswietlanieRoslinyPickComponent,
    ModalNotatkaPickComponent, RoslinaCechyContainerComponent],
  templateUrl: './offcanvas-roslina.component.html',
  styleUrl: './offcanvas-roslina.component.css'
})
export class OffcanvasRoslinaComponent {
  isCollapsed = true;
  @Input() numerDzialki: number | undefined;
  @Input() uzyt: UzytkownikResponse | undefined;
  @Input() mode: string = '';
  @Input() editMode: string = '';

  @Output() roslinaPozycjaEdit = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaKafelkiEdit = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaSmolChange = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaBaseChange = new EventEmitter<ZasadzonaRoslinaResponse>();

  @Output() roslinaRemove = new EventEmitter<ZasadzonaRoslinaResponse>();

  @ViewChild('offcanvasBottom', { static: true }) offcanvasBottom!: ElementRef;

  @ViewChild(ModalColorPickComponent) colorPickerModal!: ModalColorPickComponent;
  @ViewChild(ModalNotatkaPickComponent) notatkaPickerModal!: ModalNotatkaPickComponent;
  @ViewChild(ModalWyswietlanieRoslinyPickComponent) wyswietlaniePickerModal!: ModalWyswietlanieRoslinyPickComponent;
  @ViewChild(ModalObrazPickComponent) obrazPickerModal!: ModalObrazPickComponent;

  isTextureMode = false;
  private _roslinaObraz: string | undefined;

  constructor(
    private dzialkaService: DzialkaService,
    private cechaProcessService: CechaProcessService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) { }

  private _zasadzonaRoslina: ZasadzonaRoslinaResponse | undefined;
   @Input()
   set zasadzonaRoslina(roslina: ZasadzonaRoslinaResponse | undefined) {
     this._zasadzonaRoslina = roslina;
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

  isCurrentUzytkownik(): boolean {
    if(this.tokenService && this.uzyt) {
      return this.tokenService.isCurrentUzytkownik(this.uzyt);
    }
    return false;
  }

  getWyswietlanie(): string {
    if (this.zasadzonaRoslina?.wyswietlanie === WyswietlanieRosliny.KOLOR) {
      return 'Kolor';
    } else if (this.zasadzonaRoslina?.wyswietlanie === WyswietlanieRosliny.TEKSTURA) {
      return 'Tekstura';
    } else {
      return 'Tekstura z kolorem';
    }
  }


  removeRoslinaFromDzialka(): void {
    if(!confirm('Czy na pewno chcesz usunąć roślinę z działki?')) {
      return;
    }

    if(!this.zasadzonaRoslina || this.zasadzonaRoslina.x == undefined || this.zasadzonaRoslina.y == undefined) {
      console.error('Nie można usunąć rośliny z działki, brak pozycji');
      return;
    }
    let deletRequest = this.makeBaseDzialkaRequest();

    this.dzialkaService.deleteRoslinaFromDzialka( { body: deletRequest }).subscribe({
      next: () => {
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
    if(!this.zasadzonaRoslina) return;
    this.zasadzonaRoslina!.kolor = newColor;

    let request: DzialkaRoslinaRequest = this.makeDzialkaRoslinaRequest();
    request.kolor = newColor;

    this.dzialkaService .updateRoslinaKolorInDzialka( { body: request }).subscribe({
      next: () => {
        this.zasadzonaRoslina!.kolor = newColor;
        this.roslinaBaseChange.emit(this.zasadzonaRoslina);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  openNotatkaPickerModal() {
    this.notatkaPickerModal.openPickerModal();
  }

  confirmNotatkaPickerChange(newNotatka: string) {
    if(!this.zasadzonaRoslina) return;
    this.zasadzonaRoslina!.notatka = newNotatka;

    let request: DzialkaRoslinaRequest = this.makeDzialkaRoslinaRequest();
    request.notatka = newNotatka;

    this.dzialkaService .updateRoslinaNotatkaInDzialka( { body: request }).subscribe({
      next: () => {
        this.zasadzonaRoslina!.notatka = newNotatka;
        this.roslinaBaseChange.emit(this.zasadzonaRoslina);
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
    if(!this.zasadzonaRoslina) return;

    let request: DzialkaRoslinaRequest = this.makeDzialkaRoslinaRequest();
    request.wyswietlanie = newWyswietlanie;

    this.dzialkaService .updateRoslinaWyswietlanieInDzialka( { body: request }).subscribe({
      next: () => {
        this.zasadzonaRoslina!.wyswietlanie = newWyswietlanie;
        this.roslinaSmolChange.emit(this.zasadzonaRoslina);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  confirmObrazChange(obraz: any) {
    if(!this.zasadzonaRoslina) return;
    if(obraz === null) {
      let deletRequest = this.makeBaseDzialkaRequest();
      if(this.isTextureMode) {
        this.dzialkaService.deleteRoslinaTeksturaFromDzialka( { body: deletRequest }).subscribe({
          next: () => {
            this.roslinaSmolChange.emit(this.zasadzonaRoslina);
          },
          error: (err) => {
            console.error(err);
          }
        });
      } else {
        this.dzialkaService.deleteRoslinaObrazFromDzialka( { body: deletRequest }).subscribe({
          next: () => {
            this.zasadzonaRoslina!.obraz = this.zasadzonaRoslina?.roslina?.obraz;

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

      this.dzialkaService.updateRoslinaObrazInDzialka( { body: {request : request, obraz: obrazek, tekstura: teksturka } }).subscribe({
        next: (obrazek) => {
          //if(!this.isTextureMode) this.zasadzonaRoslina!.obraz = obrazek.content;
          this.roslinaSmolChange.emit(this.zasadzonaRoslina);
        },
        error: (err) => {
          console.error(err);
        }
      });
    }
  }

  goToMoveRoslina() {
    this.router.navigate(['ogrod', this.tokenService.nazwa,
      'dzialka', this.numerDzialki,
      'przenoszenie', this.zasadzonaRoslina?.roslina?.uuid])
      // .then(() => {
      //   window.location.reload();
      // })
      ;
  }


  private makeDzialkaRoslinaRequest(): DzialkaRoslinaRequest {
    return {
      roslinaUUID: this.zasadzonaRoslina!.roslina!.uuid!,
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
