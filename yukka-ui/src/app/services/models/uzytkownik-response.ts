/* tslint:disable */
/* eslint-disable */
import { Ustawienia } from '../models/ustawienia';
export interface UzytkownikResponse {
  aktywowany?: boolean;
  avatar?: string;
  ban?: boolean;
  banDo?: string;
  banPowod?: string;
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
  uuid?: string;
}
