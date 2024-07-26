import { AffineMatrix, Collider, Animation, Graphic, Sprite, SpriteSheet, Vector, ImageSource } from "excalibur";
import { TiledTile, TiledTileset } from "../parser/tiled-parser";
import { Properties } from "./properties";
import { PluginObject } from "./objects";
export interface TileOptions {
    id: number;
    tileset: Tileset;
    tiledTile: TiledTile;
    image?: ImageSource;
}
/**
 * Friendly plugin representation of tiled Tile
 */
export declare class Tile implements Properties {
    id: number;
    tileset: Tileset;
    tiledTile: TiledTile;
    class?: string;
    graphic?: Graphic;
    objects: PluginObject[];
    colliders: Collider[];
    animation: {
        tileid: number;
        duration: number;
    }[];
    properties: Map<string, string | number | boolean>;
    constructor(options: TileOptions);
}
export interface TilesetOptions {
    name: string;
    tiledTileset: TiledTileset;
    firstGid: number;
    image?: ImageSource;
    tileToImage?: Map<TiledTile, ImageSource>;
}
/**
 * Friendly plugin representation of Tiled tilesets
 */
export declare class Tileset implements Properties {
    name: string;
    class?: string;
    firstGid: number;
    tileCount: number;
    tiledTileset: TiledTileset;
    tileWidth: number;
    tileHeight: number;
    tileOffset: Vector;
    spritesheet: SpriteSheet;
    tiles: Tile[];
    objectalignment: string;
    orientation: 'isometric' | 'orthogonal';
    properties: Map<string, string | number | boolean>;
    horizontalFlipTransform: AffineMatrix;
    verticalFlipTransform: AffineMatrix;
    diagonalFlipTransform: AffineMatrix;
    constructor(options: TilesetOptions);
    getTilesetAlignmentAnchor(overrideAlignment?: string): Vector;
    /**
     * Returns any specially configured tiles by gid, Tiled assigns a different id to tiles
     * this helps retrieve tiles by a more common id
     * @param gid
     */
    getTileByGid(gid: number): Tile | undefined;
    getTilesByClassName(className: string): Tile[];
    getTilesByProperty(name: string, value?: any): Tile[];
    getSpriteForGid(gid: number): Sprite;
    private _isometricTiledCoordToWorld;
    /**
     * Returns any excalibur colliders setup for a Tile by gid
     *
     * By default it returns the collider in local coordinates, but sometimes you might need the collider in world coordinates
     *
     * Currently only supports Polygons, Boxes, and Ellipses*
     *
     * - Note: Ellipses can only be circles, the minimum dimension will be used to make a circle.
     * @param gid
     */
    getCollidersForGid(gid: number, options?: {
        anchor?: Vector;
        scale?: Vector;
        orientationOverride?: 'isometric' | 'orthogonal';
        offset?: Vector;
    }): Collider[];
    /**
     * Transforms points based on any gid transformations
     * @param points
     * @param gid
     */
    private _applyFlipsToPoints;
    getAnimationForGid(gid: number): Animation | null;
}
