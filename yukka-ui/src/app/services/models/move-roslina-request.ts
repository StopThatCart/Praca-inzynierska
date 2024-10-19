/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
export interface MoveRoslinaRequest {
  nazwaLacinska?: string;
  numerDzialkiNowy?: number;
  numerDzialkiStary: number;
  pozycje: Array<Pozycja>;
  roslinaId?: string;
  xnowy?: number;
  xstary?: number;
  ynowy?: number;
  ystary?: number;
}
