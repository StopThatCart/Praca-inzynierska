import { ImageSource, Loadable } from "excalibur";
import { TiledParser } from "../parser/tiled-parser";
import { FileLoader } from "./file-loader";
import { LoaderCache } from "./loader-cache";
import { PathMap } from "./path-util";
import { Template } from "./template";
export interface TemplateResourceOptions {
    headless?: boolean;
    strict?: boolean;
    parser?: TiledParser;
    fileLoader?: FileLoader;
    imageLoader?: LoaderCache<ImageSource>;
    pathMap?: PathMap;
}
/**
 * Templates are basically a mini tiled resource, they have a self contained object and optionally a tileset
 *
 * They can be used to instance objects in ObjectLayers, or as part of Tile Collider definitions
 */
export declare class TemplateResource implements Loadable<Template> {
    readonly templatePath: string;
    /**
     * [[Template]] is only accessible after .load()
     *
     * Check .isLoaded() to know if it has been loaded
     */
    data: Template;
    readonly headless: boolean;
    readonly strict: boolean;
    private parser;
    private fileLoader;
    private imageLoader;
    private pathMap?;
    constructor(templatePath: string, options?: TemplateResourceOptions);
    isLoaded(): boolean;
    load(): Promise<Template>;
}
