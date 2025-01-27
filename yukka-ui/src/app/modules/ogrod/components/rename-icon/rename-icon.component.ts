import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TokenService } from '../../../../services/token/token.service';
import { DzialkaService, OgrodService, UzytkownikService } from '../../../../services/services';
import { RenameIconModes } from './rename-icon-mode';
import { DzialkaModes } from '../../models/dzialka-modes';
import { ErrorMsgComponent } from '../../../../components/error-msg/error-msg.component';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { UzytkownikResponse } from '../../../../services/models';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-rename-icon',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent],
  templateUrl: './rename-icon.component.html',
  styleUrl: './rename-icon.component.css'
})
export class RenameIconComponent implements OnInit {
  errorMsg: Array<string> = [];

  isEditing = false;

  oldNazwa: string | undefined;
  @Input() nazwa: string | undefined;
  @Input() numer: number | undefined;
  @Input() zmieniany: RenameIconModes | undefined;

  @Output() nazwaEdit = new EventEmitter<String>();

  uzyt: UzytkownikResponse = {};
  uzytNazwa: string | undefined;

  RenameIconModes = RenameIconModes;
  constructor(private tokenService: TokenService,
    private dzialkaService: DzialkaService,
    private ogrodService: OgrodService,
    private errorHandlingService: ErrorHandlingService,
    private uzytService: UzytkownikService,
    private route: ActivatedRoute
  ) { }
  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.uzytNazwa = params['uzytkownik-nazwa'];
      if (this.uzytNazwa) {
        this.getUzytkownikByNazwa(this.uzytNazwa);
        this.route.snapshot.data['uzytkownik-nazwa'] = this.uzytNazwa;
      }
    });
  }

  onSave() {
    if (!this.isEditing) return;

    if (this.zmieniany === RenameIconModes.DZIALKA) {
      this.renameDzialka();
    } else if (this.zmieniany === RenameIconModes.OGROD) {
      this.renameOgrod();
    } else {
      console.error('Nieznany tryb zmiany nazwy');
    }
  }

  onCancel() {
    this.isEditing = false;
    this.nazwa = this.oldNazwa;
  }

  renameDzialka() {
    if(!this.numer || !this.nazwa) return;
    this.errorMsg = [];

    this.dzialkaService.renameDzialka({numer: this.numer, nazwa: this.nazwa})
    .subscribe({
      next: () => {
        this.afterSave();
      },
      error: (err) => {
        console.error(err);
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        this.nazwa = this.oldNazwa;
      }
    });
  }

  renameOgrod() {
    if(!this.nazwa) return;
    this.errorMsg = [];

    this.ogrodService.setOgrodNazwa({nazwa: this.nazwa})
    .subscribe({
      next: () => {
        this.afterSave();
      },
      error: (err) => {
        console.error(err);
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        this.nazwa = this.oldNazwa;
      }
    });

    this.nazwaEdit.emit(this.nazwa);
    this.isEditing = false;
  }

  afterSave() {
    this.nazwaEdit.emit(this.nazwa);
    this.oldNazwa = this.nazwa;
    this.isEditing = false;
  }

  getUzytkownikByNazwa(nazwa: string): void {
    this.uzytService.findByNazwa({ nazwa: nazwa }).subscribe({
      next: (uzyt) => {
        this.uzyt = uzyt;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  isCurrentUzytkownik(): boolean {
    if(this.tokenService.isTokenValid() && this.uzyt) {
      return this.tokenService.isCurrentUzytkownik(this.uzyt);
    }
    return false;
  }

}
