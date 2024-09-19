/* tslint:disable */
/* eslint-disable */
export interface UzytkownikResponse {
  avatar?: string;
  ban?: boolean;
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
  uzytId?: string;
}
