/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
export interface DzialkaRoslinaRequest {
  nazwaLacinska?: string;
  numerDzialki: number;
  obraz?: string;
  pozycje: Array<Pozycja>;
  roslinaId?: string;
  x: number;
  y: number;
}
