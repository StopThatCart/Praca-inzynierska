/* tslint:disable */
/* eslint-disable */
import { Ustawienia } from '../models/ustawienia';
export interface UzytkownikResponse {
  avatar?: string;
  ban?: boolean;
  banDo?: string;
  blokowaniUzytkownicy?: Array<UzytkownikResponse>;
  blokujacyUzytkownicy?: Array<UzytkownikResponse>;
  dataUtworzenia?: string;
  email?: string;
  id?: number;
  imie?: string;
  labels?: Array<string>;
  miasto?: string;
  miejsceZamieszkania?: string;
  nazwa?: string;
  opis?: string;
  ustawienia?: Ustawienia;
  uzytId?: string;
}
