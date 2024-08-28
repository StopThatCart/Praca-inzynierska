/* tslint:disable */
/* eslint-disable */
import { KomentarzSimpleResponse } from '../models/komentarz-simple-response';
export interface RozmowaPrywatnaResponse {
  aktywna?: boolean;
  id?: number;
  komentarze?: Array<KomentarzSimpleResponse>;
  liczbaWiadomosci?: number;
  nadawca?: string;
  ostatnioAktualizowana?: string;
  uzytkownicy?: Array<string>;
}
