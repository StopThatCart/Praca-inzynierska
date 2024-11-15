/* tslint:disable */
/* eslint-disable */
import { ZasadzonaRoslinaResponse } from '../models/zasadzona-roslina-response';
export interface DzialkaResponse {
  id?: number;
  liczbaRoslin?: number;
  nazwa?: string;
  numer?: number;
  wlascicielNazwa?: string;
  zasadzoneRosliny?: Array<ZasadzonaRoslinaResponse>;
}
