import { BaseAlign, Font, TextAlign, Vector } from "excalibur";
import { Text as ExText } from 'excalibur';
import { TiledObject, TiledObjectGroup, TiledText } from "../parser/tiled-parser";
import { Properties } from "./properties";
import { Template } from "./template";
import { TiledResource } from "./tiled-resource";
export interface PluginObjectProps {
    tiledObject: TiledObject;
}
/**
 * Plugin object represents the base class of a generic instance of an object used in a map.
 *
 * It could be more specific as well.
 */
export declare class PluginObject implements Properties {
    id: number;
    x: number;
    y: number;
    name?: string;
    class?: string;
    tiledObject: TiledObject;
    properties: Map<string, string | number | boolean>;
    constructor(props: PluginObjectProps);
}
/**
 * Represents a template instance in a map.
 *
 * Inherits properties, class, and name from template if not overridden.
 */
export declare class TemplateObject extends PluginObject {
    source: string;
    template: Template;
    tiledTemplate: TiledObject;
    constructor(tiledObject: TiledObject, template: Template);
}
/**
 * Represents an instance of an inserted tile object in a map.
 */
export declare class InsertedTile extends PluginObject {
    readonly gid: number;
    readonly width: number;
    readonly height: number;
    constructor(tiledObject: TiledObject, gid: number, width: number, height: number);
}
/**
 * Represents an instance of a point object in a map
 */
export declare class Point extends PluginObject {
}
/**
 * Represents an instance of a Text object in a map
 */
export declare class Text extends PluginObject {
    text: ExText;
    font: Font;
    constructor(tiledObject: TiledObject, text: TiledText, width: number, textQuality: number);
    _textBaselineFromTiled(code: Pick<TiledText, 'valign'>['valign']): BaseAlign.Top | BaseAlign.Middle | BaseAlign.Bottom;
    _textAlignFromTiled(code: Pick<TiledText, 'halign'>['halign']): TextAlign.Left | TextAlign.Right | TextAlign.Center | TextAlign.Start;
}
/**
 * Represents an instance of an ellipse object in a map.
 */
export declare class Ellipse extends PluginObject {
    readonly width: number;
    readonly height: number;
    constructor(tiledObject: TiledObject, width: number, height: number);
}
/**
 * Represents an instance of a rectangle object in a map.
 */
export declare class Rectangle extends PluginObject {
    readonly width: number;
    readonly height: number;
    readonly anchor: Vector;
    constructor(tiledObject: TiledObject, width: number, height: number, anchor: Vector);
}
/**
 * Represents an instance of a polygon object in a map.
 */
export declare class Polygon extends PluginObject {
    /**
     * Transformed world space points
     */
    readonly points: Vector[];
    /**
     * Local space points
     */
    readonly localPoints: Vector[];
    constructor(tiledObject: TiledObject, points: {
        x: number;
        y: number;
    }[]);
}
/**
 * Represents an instance of a polyline object in a map
 */
export declare class Polyline extends PluginObject {
    readonly points: Vector[];
    constructor(tiledObject: TiledObject, points: {
        x: number;
        y: number;
    }[]);
}
export type ObjectTypes = Polygon | Polyline | Rectangle | Ellipse | Text | Point | InsertedTile | PluginObject;
/**
 * Parses a TiledObject and returns a friendly plugin object.
 * @param object
 * @param resource
 * @returns
 */
export declare function parseObject(object: TiledObject, resource?: TiledResource): PluginObject;
/**
 * Parses a Tiled Object Group and returns friendly plugin objects.
 * @param tiledObjectGroup
 * @param resource
 * @returns
 */
export declare function parseObjects(tiledObjectGroup: TiledObjectGroup, resource?: TiledResource): PluginObject[];
