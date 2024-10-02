/* tslint:disable */
/* eslint-disable */
import { KomentarzSimpleResponse } from '../models/komentarz-simple-response';
import { UzytkownikResponse } from '../models/uzytkownik-response';
export interface RozmowaPrywatnaResponse {
  aktywna?: boolean;
  id?: number;
  komentarze?: Array<KomentarzSimpleResponse>;
  liczbaWiadomosci?: number;
  nadawca?: string;
  ostatnioAktualizowana?: string;
  uzytkownicy?: Array<UzytkownikResponse>;
}
