/* tslint:disable */
/* eslint-disable */
import { RozmowaPrywatnaResponse } from '../models/rozmowa-prywatna-response';
export interface PageResponseRozmowaPrywatnaResponse {
  content?: Array<RozmowaPrywatnaResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
