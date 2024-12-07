/* tslint:disable */
/* eslint-disable */
import { Komentarz } from '../models/komentarz';
import { Uzytkownik } from '../models/uzytkownik';
export interface RozmowaPrywatna {
  aktywna?: boolean;
  dataUtworzenia?: string;
  id?: number;
  nadawca?: string;
  ostatnioAktualizowana?: string;
  uzytkownicy?: Array<Uzytkownik>;
  wiadomosci?: Array<Komentarz>;
}
