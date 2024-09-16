import { AfterViewChecked, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { AddKomentarzCardComponent } from "../../../post/components/add-komentarz-card/add-komentarz-card.component";
import { WiadomoscCardComponent } from "../../components/wiadomosc-card/wiadomosc-card.component";
import { PageResponseRozmowaPrywatnaResponse, RozmowaPrywatna, RozmowaPrywatnaResponse, UzytkownikResponse } from '../../../../services/models';
import { RozmowaPrywatnaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { TypKomentarza } from '../../../post/enums/TypKomentarza';

@Component({
  selector: 'app-rozmowa-page',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, EdycjaNavComponent, AddKomentarzCardComponent, WiadomoscCardComponent],
  templateUrl: './rozmowa-page.component.html',
  styleUrl: './rozmowa-page.component.css'
})
export class RozmowaPageComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('scrollus') private myScrollContainer!: ElementRef;

  rozmowa: RozmowaPrywatnaResponse = {};
  odbiorcaNazwa: string | undefined;

  rozmowaUzyt : UzytkownikResponse = {};

  private _avatar: string | undefined;

  errorMessage: string | null = null;

  private intervalId: any;

  public TypKomentarza = TypKomentarza;
  constructor(
    private rozService: RozmowaPrywatnaService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.odbiorcaNazwa = params['nazwa'];
      if (this.odbiorcaNazwa) {
        this.getRozmowa(this.odbiorcaNazwa);
        this.route.snapshot.data['nazwa'] = this.odbiorcaNazwa;
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

  getRozmowa(nazwa: string): void {
    this.rozService.getRozmowaPrywatna({ nazwa: nazwa }).subscribe({
      next: (rozmowa) => {
        this.rozmowa = rozmowa;
        this.errorMessage = null;
        if(this.rozmowa.uzytkownicy) {
          const otherUzyt = this.rozmowa.uzytkownicy.find(user => user.nazwa === nazwa);
          if (otherUzyt) {
            this.rozmowaUzyt = otherUzyt;
            this._avatar = 'data:image/jpeg;base64,' + otherUzyt.avatar;
          }
        }
      },
      error: (err) => {
        this.errorMessage = 'Nie znaleziono rozmowy o podanym ID.';
      }
    });
  }

  getAvatar(): string | undefined {
    return this._avatar;
  }

  getNazwa(): string | undefined {
    return this.rozmowaUzyt.nazwa;
  }



  handleNewMessage(newMessage: any) {
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
      console.log('Scrolling to bottom');
      try {
        this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
      } catch(err) {
        console.error('Scroll to bottom failed', err);
      }
    }
  }

}
