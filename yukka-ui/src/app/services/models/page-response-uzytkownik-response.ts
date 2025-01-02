/* tslint:disable */
/* eslint-disable */
import { UzytkownikResponse } from '../models/uzytkownik-response';
export interface PageResponseUzytkownikResponse {
  content?: Array<UzytkownikResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
