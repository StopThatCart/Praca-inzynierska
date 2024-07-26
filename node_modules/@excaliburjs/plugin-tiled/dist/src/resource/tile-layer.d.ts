import { TileMap, Vector, Tile as ExTile } from "excalibur";
import { TiledTileLayer } from "../parser/tiled-parser";
import { TiledResource } from "./tiled-resource";
import { Layer } from "./layer";
import { Tile } from "./tileset";
/**
 * Tile information for both excalibur and tiled tile representations
 */
export interface TileInfo {
    /**
     * Tiled based information for the tile
     */
    tiledTile?: Tile;
    /**
     * Excalibur tile abstraction
     */
    exTile: ExTile;
}
export declare class TileLayer implements Layer {
    tiledTileLayer: TiledTileLayer;
    resource: TiledResource;
    readonly order: number;
    private logger;
    readonly name: string;
    readonly class?: string;
    /**
     * Number of tiles wide
     */
    readonly width: number;
    /**
     * Number of tiles high
     */
    readonly height: number;
    properties: Map<string, string | number | boolean>;
    /**
     * Original list of gids for this layer from tiled
     */
    data: number[];
    /**
     * Excalibur TileMap structure for drawing in excalibur
     */
    tilemap: TileMap;
    private _gidToTileInfo;
    /**
     * Returns the excalibur tiles that match a tiled gid
     */
    getTilesByGid(gid: number): TileInfo[];
    /**
     * Returns the excalibur tiles that match a tiled class name
     * @param className
     */
    getTilesByClassName(className: string): TileInfo[];
    /**
     * Returns the excalibur tiles that match a tiled property and optional value
     * @param name
     * @param value
     */
    getTilesByProperty(name: string, value?: any): TileInfo[];
    getTileByPoint(worldPos: Vector): TileInfo | null;
    getTileByCoordinate(x: number, y: number): TileInfo | null;
    constructor(tiledTileLayer: TiledTileLayer, resource: TiledResource, order: number);
    private _recordTileData;
    private updateTile;
    load(): Promise<void>;
}
