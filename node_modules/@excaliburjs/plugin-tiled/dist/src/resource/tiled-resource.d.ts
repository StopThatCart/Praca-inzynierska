import { Entity, Loadable, Scene, Vector } from "excalibur";
import { TiledMap, TiledParser } from "../parser/tiled-parser";
import { Tile, Tileset } from "./tileset";
import { Layer } from "./layer";
import { IsometricTileInfo } from "./iso-tile-layer";
import { TileInfo } from "./tile-layer";
import { IsoTileLayer } from "./iso-tile-layer";
import { TileLayer } from "./tile-layer";
import { ObjectLayer } from './object-layer';
import { Template } from "./template";
import { PathMap } from "./path-util";
import { PluginObject } from "./objects";
import { FileLoader } from './file-loader';
import { ImageLayer } from "./image-layer";
export interface TiledAddToSceneOptions {
    pos: Vector;
}
export interface TiledResourceOptions {
    /**
     * Plugin will operate in headless mode and skip all graphics related
     * excalibur items including creating ImageSource's for Tiled items.
     *
     * Default false.
     */
    headless?: boolean;
    /**
     * Add a starting z index for the layers to use. By default the layers count up from 0.
     *
     * If you'd like to manually override a z-index on a layer use the 'zindex' custom property on a layer.
     */
    startZIndex?: number;
    /**
     * Default true. If false, only tilemap will be parsed and displayed, it's up to you to wire up any excalibur behavior.
     * Automatically wires excalibur to the following
     * * Wire up current scene camera
     * * Make Actors/Tiles with colliders on Tiled tiles & Tiled objects
     * * Support solid layers
     *
     * Read more at excaliburjs.com!
     */
    useExcaliburWiring?: boolean;
    /**
     * Sets excalibur's background color to match the Tiled map
     */
    useMapBackgroundColor?: boolean;
    /**
     * Keeps the camera viewport within the bounds of the TileMap, uses the first tile layer's bounds.
     *
     * Defaults true, if false the camera will use the layer bounds to keep the camera from showing the background.
     */
    useTilemapCameraStrategy?: boolean;
    /**
     * Plugin detects the map type based on extension, if you know better you can force an override.
     */
    mapFormatOverride?: 'TMX' | 'TMJ';
    /**
     * The pathMap helps work around odd things bundlers do with static files by providing a way to redirect the original
     * source paths in the Tiled files to new locations.
     *
     * When the Tiled resource comes across something that matches `path`, it will use the output string instead.
     *
     * Example:
     * ```typescript
     * const newResource = new TiledResource('./example-city.tmx', {
     *     pathMap: [
     *        // If the "path" is included in the source path, the output will be used
     *        { path: 'cone.tx', output: '/static/assets/cone.tx' },
     *        // Regex matching with special [match] in output string that is replaced with the first match from the regex
     *        { path: /(.*\..*$)/, output: '/static/assets/[match]'}
     *     ]
     *  }
     * ```
     */
    pathMap?: PathMap;
    /**
     * Optionally provide a custom file loader implementation instead of using the built in ajax (fetch) loader
     * that takes a path and returns file data
     *
     */
    fileLoader?: FileLoader;
    /**
     * By default `true`, means Tiled files must pass the plugins Typed parse pass.
     *
     * If you have something that the Tiled plugin does not expect, you can set this to false and it will do it's best
     * to parse the Tiled source map file.
     */
    strict?: boolean;
    /**
     * Configure the text quality to use in Excalibur's Text implementation for the Tiled resources that involve text
     *
     * By default it's 4 for 4x scaled bitmap
     */
    textQuality?: number;
    /**
     * Configure custom Actor/Entity factory functions to construct Actors/Entities
     * given a Tiled class name.
     */
    entityClassNameFactories?: Record<string, (props: FactoryProps) => Entity | undefined>;
}
export interface FactoryProps {
    /**
     * Excalibur world position
     */
    worldPos: Vector;
    /**
     * Tiled name in UI
     */
    name?: string;
    /**
     * Tiled class in UI (internally in Tiled is represented as the string 'type')
     */
    class: string;
    /**
     * Layer (either TileLayer or ObjectLayer) that this object is part of
     */
    layer: Layer;
    /**
     * If using an object layer or a tile object property, the object will be passed.
     */
    object?: PluginObject;
    /**
     * Tiled properties, these are all converted to lowercase keys, and lowercase if the value is a string
     */
    properties: Record<string, any>;
}
export declare class TiledResource implements Loadable<any> {
    readonly path: string;
    private logger;
    /**
     * Currently the latest tested and supported version of Tiled
     * with the Excalibur Tiled plugin
     */
    static supportedTiledVersion: string;
    data: any;
    /**
     * The original tiled map representation in Tiled JSON/TMJ format
     *
     * If loaded from a Tiled TMX file, it has been converted to the same JSON/TMJ format
     */
    map: TiledMap;
    /**
     * A list of Tilesets from Tiled in a friendly data structure (original TSX/TSJ is available on that type)
     */
    tilesets: Tileset[];
    /**
     * A list of Templates from Tiled in a friendly data structure (original TX/TJ is available on that type)
     */
    templates: Template[];
    /**
     * A list of Layers from Tiled in a friendly data structure (original layer format is available on that type)
     *
     * Layers can either be an ObjectLayer, TileLayer, or ImageLayer
     *
     * GroupLayers don't exist in the plugin, they are flattened and the proper order preserved.
     */
    layers: Layer[];
    readonly mapFormat: 'TMX' | 'TMJ';
    readonly strict: boolean;
    factories: Map<string, (props: FactoryProps) => Entity | undefined>;
    parser: TiledParser;
    fileLoader: FileLoader;
    pathMap: PathMap | undefined;
    readonly startZIndex: number;
    readonly textQuality: number;
    readonly useExcaliburWiring: boolean;
    readonly useMapBackgroundColor: boolean;
    readonly useTilemapCameraStrategy: boolean;
    readonly headless: boolean;
    private _imageLoader;
    private _tilesetLoader;
    private _templateLoader;
    constructor(path: string, options?: TiledResourceOptions);
    /**
     * Registers an entity factory to run on load, if added after load it will be run immediately
     * @param className
     * @param factory
     */
    registerEntityFactory(className: string, factory: (props: FactoryProps) => Entity | undefined): void;
    unregisterEntityFactory(className: string): void;
    /**
     * Given a gid, find the Tileset it belongs to in the map!
     * @param gid
     * @returns
     */
    getTilesetForTileGid(gid: number): Tileset;
    /**
     * Queries for tilesets in the map by name (case insensitive)
     * @param name
     * @returns
     */
    getTilesetByName(name: string): Tileset[];
    /**
     * Queries for tilesets in the map by class name (case insensitive)
     * @param className
     * @returns
     */
    getTilesetByClassName(className: string): Tileset[];
    /**
     * Queries for tilesets in the map by property and an optional value (case insensitive)
     * @param propertyName
     * @param value
     * @returns
     */
    getTilesetByProperty(propertyName: string, value?: any): Tileset[];
    /**
     * Queries ALL tilesets tile data in the map for a specific class name (case insensitive)
     * @param className
     * @returns
     */
    getTileMetadataByClassName(className: string): Tile[];
    /**
     * Queries ALL tilesets tile data in the map for a specific property and an optional value (case insensitive)
     * @param name
     * @param value
     * @returns
     */
    getTileMetadataByProperty(name: string, value?: any): Tile[];
    /**
     * Queries ALL tile layers tile instances in the map for a specific gid
     * @param className
     * @returns
     */
    getTilesByGid(gid: number): TileInfo[] | IsometricTileInfo[];
    /**
     * Queries ALL tile layers tile instances in the map for a specific class name (case insensitive)
     * @param className
     * @returns
     */
    getTilesByClassName(className: string): TileInfo[] | IsometricTileInfo[];
    /**
     *  Queries ALL tile layers tile instances in the map for a specific property and an optional value (case insensitive)
     * @param name
     * @param value
     * @returns
     */
    getTilesByProperty(name: string, value?: any): TileInfo[] | IsometricTileInfo[];
    /**
     * Returns a tile by the world position from a layer. (Uses the first layer name that matches case insensitive).
     * @param layerName
     * @param worldPos
     * @returns
     */
    getTileByPoint(layerName: string, worldPos: Vector): TileInfo | IsometricTileInfo | null;
    /**
     * Returns a tile by the world position from a layer. (Uses the first layer name that matches case insensitive).
     * @param layerName
     * @param worldPos
     * @returns
     */
    getTilesByPoint(worldPos: Vector): TileInfo[] | IsometricTileInfo[];
    /**
     * Queries all layers for objects that match a name (case insensitive)
     * @param name
     * @returns
     */
    getObjectsByName(name: string): PluginObject[];
    getEntitiesByName(name: string): Entity[];
    getEntityByObject(object: PluginObject): Entity | undefined;
    getObjectByEntity(actor: Entity): PluginObject | undefined;
    /**
     * Search for a tiled object that has a property name, and optionally specify a value
     * @param propertyName
     * @param value
     * @returns
     */
    getObjectsByProperty(propertyName: string, value?: any): PluginObject[];
    /**
     * Search for actors that were created from tiled objects
     * @returns
     */
    getEntitiesByProperty(propertyName: string, value?: any): Entity[];
    /**
     * Search for an Tiled object by it's Tiled class name
     * @returns
     */
    getObjectsByClassName(className: string): PluginObject[];
    /**
     * Search for an Actor created by the plugin by it's Tiled object
     * @param className
     * @returns
     */
    getEntitiesByClassName(className: string): Entity[];
    /**
     * Returns a tile by x, y integer coordinate from a layer. (Uses the first layer name that matches case insensitive).
     * @param layerName
     * @param x
     * @param y
     * @returns
     */
    getTileByCoordinate(layerName: string, x: number, y: number): TileInfo | null;
    getImageLayers(name?: string): ImageLayer[];
    getTileLayers(name?: string): TileLayer[];
    getIsoTileLayers(name?: string): IsoTileLayer[];
    getObjectLayers(name?: string): ObjectLayer[];
    getLayersByName(name: string): Layer[];
    getLayersByClassName(className: string): Layer[];
    getLayersByProperty(propertyName: string, value?: any): Layer[];
    private _parseMap;
    load(): Promise<any>;
    private _collectTilesets;
    private _collectTemplates;
    addToScene(scene: Scene, options?: TiledAddToSceneOptions): void;
    isometricTiledCoordToWorld(x: number, y: number): Vector;
    isLoaded(): boolean;
}
