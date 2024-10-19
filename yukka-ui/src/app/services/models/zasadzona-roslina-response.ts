/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
import { RoslinaResponse } from '../models/roslina-response';
export interface ZasadzonaRoslinaResponse {
  obraz?: string;
  pozycje?: Array<Pozycja>;
  roslina?: RoslinaResponse;
  tabX?: Array<number>;
  tabY?: Array<number>;
  x?: number;
  y?: number;
}
