/* tslint:disable */
/* eslint-disable */
import { UzytkownikRoslinaResponse } from '../models/uzytkownik-roslina-response';
export interface PageResponseUzytkownikRoslinaResponse {
  content?: Array<UzytkownikRoslinaResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
