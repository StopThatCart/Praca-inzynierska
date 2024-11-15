/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
export interface MoveRoslinaRequest {
  numerDzialki: number;
  numerDzialkiNowy?: number;
  pozycje: Array<Pozycja>;
  x: number;
  xnowy?: number;
  y: number;
  ynowy?: number;
}
