/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
export interface BaseDzialkaRequest {
  numerDzialki: number;
  pozycje: Array<Pozycja>;
  x: number;
  y: number;
}
