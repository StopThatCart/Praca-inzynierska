import { RoslinaResponse, ZasadzonaRoslinaResponse } from "../../../services/models";

export interface Tile {
  image?: string,
  x: number,
  y: number,
  clickable: boolean,
  zasadzonaRoslina?: ZasadzonaRoslinaResponse,
  roslinaId?: string,
  backgroundColor?: string,
  hovered: boolean
}
export class TileUtils {
  static images = {
    grass: 'assets/tiles/grass.png',
    dirt: 'assets/tiles/dirt.png'
  };

  static findTile(tiles: Tile[], x: number, y: number): Tile | undefined {
    const tile = tiles.find(t => t.x === x && t.y === y);
    if (!tile) {
      return undefined;
    }
    return tile;
  }

  static findTileById(tiles: Tile[], roslinaId: string): Tile {
    const tile = tiles.find(t => t.roslinaId === roslinaId);
    if (!tile) {
      throw new Error(`Nie znaleziono kafelka z przypisanym roslinaId(${roslinaId})`);
    }
    return tile;
  }

  static clearTile(tile: Tile) {
    tile.roslinaId = undefined;
    tile.zasadzonaRoslina = undefined;
    tile.backgroundColor = undefined;
    tile.image = this.images.dirt;
  }

  static removeRoslina(tile: Tile) {
    tile.roslinaId = undefined;
    tile.zasadzonaRoslina = undefined;
    tile.image = this.images.dirt;
  }

  static base64ToBlob(base64: string, contentType: string): Blob {
    console.log('base64ToBlob');
    console.log(base64);
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    return new Blob([byteArray], { type: contentType });
  }
}
