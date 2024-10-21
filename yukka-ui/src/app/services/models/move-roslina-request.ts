/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
export interface MoveRoslinaRequest {
  nazwaLacinska?: string;
  numerDzialki: number;
  numerDzialkiNowy?: number;
  obraz?: string;
  pozycje: Array<Pozycja>;
  roslinaId?: string;
  x: number;
  xnowy?: number;
  y: number;
  ynowy?: number;
}
