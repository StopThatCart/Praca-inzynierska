/* tslint:disable */
/* eslint-disable */
import { Post } from '../models/post';
import { RozmowaPrywatna } from '../models/rozmowa-prywatna';
import { Uzytkownik } from '../models/uzytkownik';
export interface Komentarz {
  dataUtworzenia?: string;
  edytowany?: boolean;
  id?: number;
  komentarzId?: string;
  obraz?: string;
  ocenyLubiButGood?: number;
  ocenyNieLubiButGood?: number;
  odpowiadaKomentarzowi?: Komentarz;
  odpowiedzi?: Array<Komentarz>;
  opis?: string;
  post?: Post;
  rozmowaPrywatna?: RozmowaPrywatna;
  uzytkownik?: Uzytkownik;
  wposcie?: Post;
}
