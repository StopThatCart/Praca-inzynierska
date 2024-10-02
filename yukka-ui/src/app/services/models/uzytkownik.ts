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
  authorities?: Array<GrantedAuthority>;
  avatar?: string;
  ban?: boolean;
  blokowaniUzytkownicy?: Array<Uzytkownik>;
  blokujacyUzytkownicy?: Array<Uzytkownik>;
  credentialsNonExpired?: boolean;
  dataUtworzenia?: string;
  email?: string;
  enabled?: boolean;
  haslo?: string;
  id?: number;
  komentarze?: Array<Komentarz>;
  komentarzeOcenyNegatywne?: number;
  komentarzeOcenyPozytywne?: number;
  labels?: Array<string>;
  name?: string;
  nazwa?: string;
  normalUzytkownik?: boolean;
  oceny?: Array<Ocenil>;
  ogrod?: Ogrod;
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
