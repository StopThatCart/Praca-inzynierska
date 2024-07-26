import { Component } from "excalibur";
import { TiledTileLayer } from "../parser/tiled-parser";
export interface TiledLayerDataComponentOptions {
    tiledTileLayer: TiledTileLayer;
}
export declare class TiledLayerDataComponent extends Component {
    readonly tiledTileLayer: TiledTileLayer;
    constructor(options: TiledLayerDataComponentOptions);
}
