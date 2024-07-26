import { ExcaliburCamera, TiledPoint } from "./tiled-types";
import { TiledEntity } from "./tiled-entity";
import { RawTiledLayer } from "./raw-tiled-layer";
import { RawTiledObject } from "./raw-tiled-object";
/**
 * @deprecated
 */
export interface Polygon {
    x: number;
    y: number;
    polygon: {
        points: string;
    };
}
/**
 * @deprecated
 */
export interface Box {
    x: number;
    y: number;
    width: number;
    height: number;
}
/**
 * @deprecated
 */
export interface Ellipse {
    x: number;
    y: number;
    width: number;
    height: number;
}
/**
 * Tiled Object Layer
 * @deprecated
 */
export declare class TiledObjectGroup extends TiledEntity {
    objects: TiledObject[];
    rawObjectGroup: RawTiledLayer;
    order: number;
    getCamera(): ExcaliburCamera | undefined;
    getObjectByType(type: string): TiledObject | undefined;
    getObjectByClass(type: string): TiledObject | undefined;
    getObjectsByType(type: string): TiledObject[];
    /**
     * Since Tiled 1.10 the property is called `type` again.
     * We treat both properties here to be on the safe side.
     * See https://doc.mapeditor.org/en/stable/reference/tmx-changelog/#tiled-1-10
     */
    getObjectsByClass(type: string): TiledObject[];
    getObjectByName(name: string): TiledObject | undefined;
    getObjectsByName(name: string): TiledObject[];
    getPoints(): TiledObject[];
    getEllipses(): (TiledObject & Ellipse)[];
    getText(): TiledObject[];
    getPolyLines(): TiledObject[];
    getPolygons(): (TiledObject & Polygon)[];
    getBoxes(): (TiledObject & Box)[];
    getInsertedTiles(): TiledObject[];
    static parse(objectGroup: RawTiledLayer): TiledObjectGroup;
}
/**
 * @deprecated
 */
export declare class TiledObject extends TiledEntity {
    type?: string;
    class?: string;
    x: number;
    y: number;
    visible: boolean;
    rotation: number;
    width?: number;
    height?: number;
    /**
     * Present on point objects
     */
    point?: boolean;
    /**
     * Present on ellipse objects
     */
    ellipse?: boolean;
    /**
     * Present on text objects
     */
    text?: TiledText;
    /**
     * Present on polyline objects
     */
    polyline?: TiledPoint[];
    /**
     * Present on polygon objects
     */
    polygon?: TiledPoint[];
    /**
     * Present on inserted tile objects
     */
    gid?: number;
    rawObject: RawTiledObject;
    static parse(object: RawTiledObject): TiledObject;
}
/**
 * @deprecated
 */
export interface TiledText {
    text: string;
    color?: string;
    fontFamily: string;
    pixelSize: number;
    bold: boolean;
    italic: boolean;
    underline: boolean;
    strikeout: boolean;
    kerning: boolean;
}
/**
 * @deprecated
 */
export interface TiledInsertedTile extends TiledObject {
    gid: number;
}
