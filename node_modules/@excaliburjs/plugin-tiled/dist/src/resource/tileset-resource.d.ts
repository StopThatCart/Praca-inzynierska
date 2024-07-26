import { ImageSource, Loadable } from "excalibur";
import { Tileset } from "./tileset";
import { TiledParser } from "../parser/tiled-parser";
import { FileLoader } from "./file-loader";
import { PathMap } from "./path-util";
import { LoaderCache } from "./loader-cache";
export interface TilesetResourceOptions {
    orientation?: 'isometric' | 'orthogonal';
    headless?: boolean;
    strict?: boolean;
    parser?: TiledParser;
    fileLoader?: FileLoader;
    imageLoader?: LoaderCache<ImageSource>;
    pathMap?: PathMap;
}
export declare class TilesetResource implements Loadable<Tileset> {
    path: string;
    data: Tileset;
    readonly firstGid: number;
    readonly strict: boolean;
    readonly headless: boolean;
    readonly orientation: 'isometric' | 'orthogonal';
    private fileLoader;
    private imageLoader;
    private pathMap?;
    private parser;
    constructor(path: string, firstGid: number, options?: TilesetResourceOptions);
    load(): Promise<Tileset>;
    isLoaded(): boolean;
}
