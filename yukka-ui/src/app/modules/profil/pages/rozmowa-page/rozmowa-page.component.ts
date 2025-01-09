import { AfterViewChecked, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { AddKomentarzCardComponent } from "../../../social/components/add-komentarz-card/add-komentarz-card.component";
import { WiadomoscCardComponent } from "../../components/wiadomosc-card/wiadomosc-card.component";
import { PageResponseRozmowaPrywatnaResponse, RozmowaPrywatnaResponse, UzytkownikResponse } from '../../../../services/models';
import { RozmowaPrywatnaService, UzytkownikService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { TypKomentarza } from '../../../social/models/TypKomentarza';
import { ZgloszenieButtonComponent } from "../../components/zgloszenie-button/zgloszenie-button.component";
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-rozmowa-page',
  standalone: true,
  imports: [CommonModule, EdycjaNavComponent, AddKomentarzCardComponent, WiadomoscCardComponent],
  templateUrl: './rozmowa-page.component.html',
  styleUrl: './rozmowa-page.component.css'
})
export class RozmowaPageComponent implements OnInit, OnDestroy, AfterViewChecked {

  @ViewChild('scrollus') private myScrollContainer!: ElementRef;

  rozmowa: RozmowaPrywatnaResponse = {};
  wiadomoscCount: number | undefined;


  odbiorcaNazwa: string | undefined;


  zablokowany: boolean = false;
  blokujacy: boolean = false;
  blokowaniUzytkownicy: UzytkownikResponse[] = [];
  blokujacyUzytkownicy: UzytkownikResponse[] = [];



  rozmowaUzyt : UzytkownikResponse = {};

  private _avatar: string | undefined;

  errorMsg: Array<string> = [];

  private intervalId: any;

  public TypKomentarza = TypKomentarza;
  constructor(
    private rozService: RozmowaPrywatnaService,
    private uzytService: UzytkownikService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.odbiorcaNazwa = params['uzytkownik-nazwa'];
      if (this.odbiorcaNazwa) {
        this.getBlokowaniAndBlokujacy();
        this.getRozmowa(this.odbiorcaNazwa);
        this.route.snapshot.data['uzytkownik-nazwa'] = this.odbiorcaNazwa;
      }
    });

    // Co 5 sekund odpytuje serwer o nowe wiadomości. Tak, to jest okropne, ale działa.
    this.intervalId = setInterval(() => {
      if (this.odbiorcaNazwa) {
        this.getRozmowa(this.odbiorcaNazwa);
      }
    }, 5000);

   // this.findAllPowiadomienia();
   // console.log(this.powResponse);
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  getBlokowaniAndBlokujacy() {
    this.uzytService.getBlokowaniAndBlokujacy().subscribe({
      next: (response) => {
        this.blokowaniUzytkownicy = response.blokowaniUzytkownicy ?? [];
        this.blokujacyUzytkownicy = response.blokujacyUzytkownicy ?? [];
      },
      error: (error) => {
        console.error('Error fetching blokowani and blokujacy:', error);
      }
    });
  }

  getRozmowa(nazwa: string): void {
    this.errorMsg = [];
    this.rozService.findRozmowaPrywatnaByNazwa({ 'uzytkownik-nazwa': nazwa }).subscribe({
      next: (rozmowa) => {
        this.rozmowa = rozmowa;
        this.wiadomoscCount = rozmowa.komentarze?.length;
        if (!this.rozmowa.aktywna) {
          //this.router.navigate(['/profil/rozmowy']);
        }
        if(this.rozmowa.uzytkownicy) {
          const otherUzyt = this.rozmowa.uzytkownicy.find(user => user.nazwa === nazwa);
          if (otherUzyt) {
            this.rozmowaUzyt = otherUzyt;
            this._avatar = 'data:image/jpeg;base64,' + otherUzyt.avatar;
          }
        }

        this.zablokowany = this.isBlokowany(this.rozmowa.uzytkownicy);
        this.blokujacy = this.isBlokujacy(this.rozmowa.uzytkownicy);
      },
      error: (err) => {
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
      }
    });
  }

  getAvatar(): string | undefined {
    return this._avatar;
  }

  getNazwa(): string | undefined {
    return this.rozmowaUzyt.nazwa;
  }

  isBlokowany(uzytkownicy: Array<UzytkownikResponse> | undefined): boolean {
    if (uzytkownicy) {
      return uzytkownicy.some(uzytkownik =>
        this.blokowaniUzytkownicy?.some(blokowany => blokowany.nazwa === uzytkownik.nazwa)
      );
    }
    return false;
  }

  isBlokujacy(uzytkownicy: Array<UzytkownikResponse> | undefined): boolean {
    if (uzytkownicy) {
      return uzytkownicy.some(uzytkownik =>
        this.blokujacyUzytkownicy?.some(blokowany => blokowany.nazwa === uzytkownik.nazwa)
      );
    }
    return false;
  }




  handleNewMessage(newMessage: any) {
    if (this.rozmowa.komentarze) {
      if (this.odbiorcaNazwa) {
        this.getRozmowa(this.odbiorcaNazwa);
      }
      //this.rozmowa.komentarze.push(newMessage);
    }
  }

  handleRemove($event: any) {
    if (this.rozmowa.komentarze) {
      if (this.odbiorcaNazwa) {
        this.getRozmowa(this.odbiorcaNazwa);
      }
      //this.rozmowa.komentarze.push(newMessage);
    }
  }

  private isUserNearBottom(): boolean {
    const threshold = 150;
    const position = this.myScrollContainer.nativeElement.scrollTop + this.myScrollContainer.nativeElement.offsetHeight;
    const height = this.myScrollContainer.nativeElement.scrollHeight;
    return position > height - threshold;
  }

  private scrollToBottom(): void {
    if (this.isUserNearBottom()) {
      //console.log('Scrolling to bottom');
      try {
        this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
      } catch(err) {
        console.error('Scroll to bottom failed', err);
      }
    }
  }


  zablokujUzyt() {
    if(!confirm("Czy aby na pewno chcesz zablokować użytkownika? Nie będziesz mógł z nim rozmawiać, dopoki go nie odblokujesz.")) {
      return;
    }

    if (this.odbiorcaNazwa) {
      this.errorMsg = [];
      this.uzytService.setBlokUzytkownik({ nazwa: this.odbiorcaNazwa, blok: true }).subscribe({
        next: (res) => {
          if(res) {
            console.log('Użytkownik zablokowany');
            console.log(res);
            this.zablokowany = true;
            this.getBlokowaniAndBlokujacy();
          } else {
            console.log('Nie udało się zablokować użytkownika');
          }
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
      });

    }
  }

  odblokujUzyt() {
    if(!confirm("Czy aby na pewno chcesz odblokować użytkownika?")) {
      return;
    }

    if (this.odbiorcaNazwa) {
      this.errorMsg = [];
      this.uzytService.setBlokUzytkownik({ nazwa: this.odbiorcaNazwa, blok: false }).subscribe({
        next: (res) => {
          if(res) {
            console.log('Użytkownik odblokowany');
            console.log(res);
            this.zablokowany = false;
            this.getBlokowaniAndBlokujacy();
          } else {
            console.log('Nie udało się odblokować użytkownika');
          }
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
      });
    }
  }

}
