import { Component, Input } from '@angular/core';
import { UzytkownikResponse } from '../../../../services/models';
import { Router } from 'express';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-uzytkownik-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './uzytkownik-card.component.html',
  styleUrl: './uzytkownik-card.component.css'
})
export class UzytkownikCardComponent {
  @Input() uzyt: UzytkownikResponse = {};
  private _avatar: string | undefined;


  getUzytAvatar(): string | undefined {
    if(this.uzyt.avatar) {
      return 'data:image/jpeg;base64,' + this.uzyt.avatar;
    }
    return this._avatar;
  }
}
