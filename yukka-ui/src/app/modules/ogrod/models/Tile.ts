import { RoslinaResponse } from "../../../services/models";

export interface Tile {
  image?: string,
  x: number,
  y: number,
  clickable: boolean,
  roslina?: RoslinaResponse,
  roslinaId?: number,
  backgroundColor?: string,
  hovered: boolean
}
