import { TiledProperty } from "../parser/tiled-parser";
export interface Properties {
    /**
     * Tiled properties are all lowercased when imported.
     *
     * These are all converted to lowercase keys, and lowercase if the value is a string
     */
    properties: Map<string, string | number | boolean>;
}
/**
 * Maps the tiled source properties, to a JS property map with all keys/values normalized to lowercase where appropriate
 * @param target
 * @param sourceProps
 */
export declare function mapProps<T extends Properties>(target: T, sourceProps?: TiledProperty[]): void;
