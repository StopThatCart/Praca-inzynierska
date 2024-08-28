/* tslint:disable */
/* eslint-disable */
import { KomentarzResponse } from '../models/komentarz-response';
export interface PageResponseKomentarzResponse {
  content?: Array<KomentarzResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
