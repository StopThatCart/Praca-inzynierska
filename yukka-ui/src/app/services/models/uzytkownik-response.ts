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
  komentarzeOcenyNegatywne?: number;
  komentarzeOcenyPozytywne?: number;
  labels?: Array<string>;
  nazwa?: string;
  postyOcenyNegatywne?: number;
  postyOcenyPozytywne?: number;
  ustawienia?: Ustawienia;
  uzytId?: string;
}
