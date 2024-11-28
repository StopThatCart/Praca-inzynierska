/* tslint:disable */
/* eslint-disable */
import { Uzytkownik } from '../models/uzytkownik';
export interface PowiadomienieDto {
  avatar?: string;
  data?: string;
  dataUtworzenia?: string;
  id?: number;
  iloscPolubien?: number;
  nazwyRoslin?: Array<string>;
  odnosnik?: string;
  okres?: string;
  opis?: string;
  przeczytane?: boolean;
  typ?: string;
  tytul?: string;
  uzytkownikNazwa?: string;
  zglaszajacy?: Uzytkownik;
  zglaszany?: string;
  zgloszenie?: boolean;
}
