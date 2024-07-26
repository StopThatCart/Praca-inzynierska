import { Vector, IsometricMap, IsometricTile } from "excalibur";
import { TiledTileLayer } from "../parser/tiled-parser";
import { TiledResource } from "./tiled-resource";
import { Layer } from "./layer";
import { Tile } from "./tileset";
export interface IsometricTileInfo {
    /**
     * Tiled based information for the tile
     */
    tiledTile?: Tile;
    /**
     * Excalibur tile abstraction
     */
    exTile: IsometricTile;
}
export declare class IsoTileLayer implements Layer {
    tiledTileLayer: TiledTileLayer;
    resource: TiledResource;
    readonly order: number;
    private logger;
    readonly name: string;
    class?: string | undefined;
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
     * Excalibur IsometricMap structure for drawing in excalibur
     */
    isometricMap: IsometricMap;
    private _gidToTileInfo;
    constructor(tiledTileLayer: TiledTileLayer, resource: TiledResource, order: number);
    /**
     * Returns the excalibur tiles that match a tiled gid
     */
    getTilesByGid(gid: number): IsometricTileInfo[];
    /**
     * Returns the excalibur tiles that match a tiled class name
     * @param className
     */
    getTilesByClassName(className: string): IsometricTileInfo[];
    /**
     * Returns the excalibur tiles that match a tiled property and optional value
     * @param name
     * @param value
     */
    getTilesByProperty(name: string, value?: any): IsometricTileInfo[];
    getTileByPoint(worldPos: Vector): IsometricTileInfo | null;
    private _recordTileData;
    private updateTile;
    load(): Promise<void>;
}
