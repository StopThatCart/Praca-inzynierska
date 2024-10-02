/* tslint:disable */
/* eslint-disable */
import { PostResponse } from '../models/post-response';
export interface PageResponsePostResponse {
  content?: Array<PostResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
