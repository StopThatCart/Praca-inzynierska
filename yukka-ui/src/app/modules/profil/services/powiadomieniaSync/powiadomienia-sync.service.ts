import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { PowiadomienieResponse } from '../../../../services/models';

@Injectable({
  providedIn: 'root'
})
export class PowiadomieniaSyncService {
  private unreadCountUpdatedSource = new Subject<void>();
  private powiadomieniePrzeczytaneSource = new Subject<PowiadomienieResponse>();
  private powiadomienieUsunieteSource = new Subject<PowiadomienieResponse>();

  unreadCountUpdated$ = this.unreadCountUpdatedSource.asObservable();
  powiadomieniePrzeczytane$ = this.powiadomieniePrzeczytaneSource.asObservable();
  powiadomienieUsuniete$ = this.powiadomienieUsunieteSource.asObservable();

  notifyUnreadCountUpdated() {
    this.unreadCountUpdatedSource.next();
  }

  notifyPowiadomieniePrzeczytane(pow: PowiadomienieResponse) {
    this.powiadomieniePrzeczytaneSource.next(pow);
  }

  notifyPowiadomienieUsuniete(pow: PowiadomienieResponse) {
    this.powiadomienieUsunieteSource.next(pow);
  }
}
