/* tslint:disable */
/* eslint-disable */
import { UzytkownikResponse } from '../models/uzytkownik-response';
export interface PowiadomienieResponse {
  avatar?: string;
  data?: string;
  dataUtworzenia?: string;
  id?: number;
  iloscPolubien?: number;
  nazwyRoslin?: Array<string>;
  odnosnik?: string;
  opis?: string;
  przeczytane?: boolean;
  typ?: string;
  tytul?: string;
  uzytkownikNazwa?: string;
  zglaszajacy?: UzytkownikResponse;
  zgloszenie?: boolean;
}
