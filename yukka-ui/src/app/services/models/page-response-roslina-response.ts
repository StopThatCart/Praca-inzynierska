/* tslint:disable */
/* eslint-disable */
import { RoslinaResponse } from '../models/roslina-response';
export interface PageResponseRoslinaResponse {
  content?: Array<RoslinaResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
