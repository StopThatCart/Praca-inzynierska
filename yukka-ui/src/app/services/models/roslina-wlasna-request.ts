/* tslint:disable */
/* eslint-disable */
import { CechaWithRelations } from '../models/cecha-with-relations';
export interface RoslinaWlasnaRequest {
  cechy: Array<CechaWithRelations>;
  nazwa: string;
  obraz?: string;
  opis: string;
  roslinaId?: string;
  wysokoscMax: number;
  wysokoscMin: number;
}
