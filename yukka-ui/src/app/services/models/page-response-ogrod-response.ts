/* tslint:disable */
/* eslint-disable */
import { OgrodResponse } from '../models/ogrod-response';
export interface PageResponseOgrodResponse {
  content?: Array<OgrodResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
