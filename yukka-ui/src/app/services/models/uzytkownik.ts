/* tslint:disable */
/* eslint-disable */
import { GrantedAuthority } from '../models/granted-authority';
import { Komentarz } from '../models/komentarz';
import { Ocenil } from '../models/ocenil';
import { Ogrod } from '../models/ogrod';
import { Post } from '../models/post';
import { RozmowaPrywatna } from '../models/rozmowa-prywatna';
import { Ustawienia } from '../models/ustawienia';
export interface Uzytkownik {
  accountNonExpired?: boolean;
  accountNonLocked?: boolean;
  admin?: boolean;
  aktywowany?: boolean;
  authorities?: Array<GrantedAuthority>;
  avatar?: string;
  ban?: boolean;
  banDo?: string;
  blokowaniUzytkownicy?: Array<Uzytkownik>;
  blokujacyUzytkownicy?: Array<Uzytkownik>;
  credentialsNonExpired?: boolean;
  dataUtworzenia?: string;
  email?: string;
  enabled?: boolean;
  haslo?: string;
  id?: number;
  imie?: string;
  komentarze?: Array<Komentarz>;
  komentarzeOcenyNegatywne?: number;
  komentarzeOcenyPozytywne?: number;
  labels?: Array<string>;
  miasto?: string;
  miejsceZamieszkania?: string;
  name?: string;
  nazwa?: string;
  normalUzytkownik?: boolean;
  oceny?: Array<Ocenil>;
  ogrod?: Ogrod;
  opis?: string;
  password?: string;
  posty?: Array<Post>;
  postyOcenyNegatywne?: number;
  postyOcenyPozytywne?: number;
  pracownik?: boolean;
  rozmowyPrywatne?: Array<RozmowaPrywatna>;
  username?: string;
  ustawienia?: Ustawienia;
  uzytId?: string;
}
