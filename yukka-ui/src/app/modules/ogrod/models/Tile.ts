import { RoslinaResponse, ZasadzonaRoslinaResponse } from "../../../services/models";

export interface Tile {
  image?: string,
  x: number,
  y: number,
  clickable: boolean,
  zasadzonaRoslina?: ZasadzonaRoslinaResponse,
  roslina?: RoslinaResponse,
  roslinaId?: number,
  backgroundColor?: string,
  hovered: boolean
}
export class TileUtils {
  static findTile(tiles: Tile[], x: number, y: number): Tile {
    const tile = tiles.find(t => t.x === x && t.y === y);
    if (!tile) {
      throw new Error(`Nie znaleziono kafelka na koordynatach(${x}, ${y})`);
    }
    return tile;
  }

  static findTileById(tiles: Tile[], id: number): Tile {
    const tile = tiles.find(t => t.roslinaId === id);
    if (!tile) {
      throw new Error(`Nie znaleziono kafelka z przypisanym id(${id})`);
    }
    return tile;
  }

  static clearTile(tile: Tile) {
    tile.roslinaId = undefined;
    tile.zasadzonaRoslina = undefined;
    tile.roslina = undefined;
    tile.backgroundColor = undefined;
  }
}
