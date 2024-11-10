/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
export interface DzialkaRoslinaRequest {
  kolor: string;
  nazwaLacinska?: string;
  numerDzialki: number;
  obraz?: string;
  pozycje: Array<Pozycja>;
  roslinaId?: string;
  tekstura?: string;
  wyswietlanie: string;
  x: number;
  y: number;
}
