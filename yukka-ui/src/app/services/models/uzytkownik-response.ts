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
  komentarzeOcenyNegatywne?: number;
  komentarzeOcenyPozytywne?: number;
  labels?: Array<string>;
  miasto?: string;
  miejsceZamieszkania?: string;
  nazwa?: string;
  opis?: string;
  postyOcenyNegatywne?: number;
  postyOcenyPozytywne?: number;
  ustawienia?: Ustawienia;
  uzytId?: string;
}
