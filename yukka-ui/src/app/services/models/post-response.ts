/* tslint:disable */
/* eslint-disable */
import { KomentarzResponse } from '../models/komentarz-response';
export interface PostResponse {
  avatar?: string;
  dataUtworzenia?: string;
  id?: number;
  komentarze?: Array<KomentarzResponse>;
  liczbaKomentarzy?: number;
  obraz?: string;
  ocenyLubi?: number;
  ocenyNieLubi?: number;
  opis?: string;
  tytul?: string;
  uuid?: string;
  uzytkownik?: string;
}
