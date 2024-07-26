import { Actor, ImageSource } from "excalibur";
import { Layer } from "./layer";
import { TiledImageLayer } from "../parser/tiled-parser";
import { TiledResource } from "./tiled-resource";
export declare class ImageLayer implements Layer {
    tiledImageLayer: TiledImageLayer;
    resource: TiledResource;
    readonly order: number;
    readonly name: string;
    readonly class?: string;
    properties: Map<string, string | number | boolean>;
    image: ImageSource | null;
    imageActor: Actor | null;
    constructor(tiledImageLayer: TiledImageLayer, resource: TiledResource, order: number);
    load(): Promise<void>;
}
