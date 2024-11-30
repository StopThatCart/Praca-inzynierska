/* tslint:disable */
/* eslint-disable */
import { WlasciwoscWithRelations } from '../models/wlasciwosc-with-relations';
export interface UzytkownikRoslinaRequest {
  nazwa: string;
  obraz?: string;
  opis: string;
  roslinaId?: string;
  wlasciwosci: Array<WlasciwoscWithRelations>;
  wysokoscMax: number;
  wysokoscMin: number;
}
