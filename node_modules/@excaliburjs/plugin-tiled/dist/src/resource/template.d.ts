import { TiledTemplate } from "../parser/tiled-parser";
import { PluginObject } from "./objects";
import { Tileset } from "./tileset";
export interface TemplateOptions {
    templatePath: string;
    object: PluginObject;
    tiledTemplate: TiledTemplate;
    tileset?: Tileset;
}
/**
 * Templates are basically a mini tiled resource, they have a self contained object and optionally a tileset
 *
 * They can be used to instance objects in ObjectLayers, or as part of Tile Collider definitions
 */
export declare class Template {
    templatePath: string;
    tiledTemplate: TiledTemplate;
    object: PluginObject;
    tileset?: Tileset;
    constructor(options: TemplateOptions);
}
