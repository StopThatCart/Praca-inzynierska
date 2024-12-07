/* tslint:disable */
/* eslint-disable */
import { Komentarz } from '../models/komentarz';
import { OcenilReverse } from '../models/ocenil-reverse';
import { Uzytkownik } from '../models/uzytkownik';
export interface Post {
  autor?: Uzytkownik;
  dataUtworzenia?: string;
  id?: number;
  komentarze?: Array<Komentarz>;
  komentarzeWPoscie?: Array<Komentarz>;
  obraz?: string;
  ocenil?: Array<OcenilReverse>;
  ocenyLubiButGood?: number;
  ocenyNieLubiButGood?: number;
  opis?: string;
  postId?: string;
  tytul?: string;
}
