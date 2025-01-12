/* tslint:disable */
/* eslint-disable */
import { CechaWithRelations } from '../models/cecha-with-relations';
export interface RoslinaRequest {
  cechy: Array<CechaWithRelations>;
  nazwa: string;
  nazwaLacinska: string;
  obraz?: string;
  opis: string;
  uuid?: string;
  wysokoscMax: number;
  wysokoscMin: number;
}
