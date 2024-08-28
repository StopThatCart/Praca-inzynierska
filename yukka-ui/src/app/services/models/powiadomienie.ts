/* tslint:disable */
/* eslint-disable */
import { Uzytkownik } from '../models/uzytkownik';
export interface Powiadomienie {
  avatar?: string;
  data?: string;
  dataUtworzenia?: string;
  id?: number;
  iloscPolubien?: number;
  nazwyRoslin?: Array<string>;
  odnosnik?: string;
  opis?: string;
  typ?: string;
  tytul?: string;
  uzytkownik?: Uzytkownik;
  uzytkownikNazwa?: string;
}
