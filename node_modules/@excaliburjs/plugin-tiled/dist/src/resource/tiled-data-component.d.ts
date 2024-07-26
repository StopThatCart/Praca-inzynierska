import { Component } from "excalibur";
import { PluginObject } from "./objects";
export interface TiledDataComponentOptions {
    tiledObject: PluginObject;
}
export declare class TiledDataComponent extends Component {
    tiledObject: PluginObject;
    constructor(options: TiledDataComponentOptions);
}
