/* tslint:disable */
/* eslint-disable */
import { WlasciwoscWithRelations } from '../models/wlasciwosc-with-relations';
export interface RoslinaRequest {
  nazwa: string;
  nazwaLacinska: string;
  obraz: string;
  opis: string;
  roslinaId?: string;
  wlasciwosci: Array<WlasciwoscWithRelations>;
  wysokoscMin: number;
  wysokoscMax: number;
}
