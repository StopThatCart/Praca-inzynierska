/* tslint:disable */
/* eslint-disable */
import { Komentarz } from '../models/komentarz';
import { Ocenil } from '../models/ocenil';
import { Uzytkownik } from '../models/uzytkownik';
export interface Post {
  autor?: Uzytkownik;
  dataUtworzenia?: string;
  id?: number;
  komentarze?: Array<Komentarz>;
  liczbaKomentarzy?: number;
  obraz?: string;
  ocenil?: Array<Ocenil>;
  ocenyLubi?: number;
  ocenyNieLubi?: number;
  opis?: string;
  postId?: string;
  tytul?: string;
}
