/* tslint:disable */
/* eslint-disable */
import { PowiadomienieResponse } from '../models/powiadomienie-response';
export interface PageResponsePowiadomienieResponse {
  content?: Array<PowiadomienieResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
