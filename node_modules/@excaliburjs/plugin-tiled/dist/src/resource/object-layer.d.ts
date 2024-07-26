import { Actor, Entity } from "excalibur";
import { Layer } from "./layer";
import { PluginObject, TemplateObject } from "./objects";
import { TiledObjectLayer } from "../parser/tiled-parser";
import { TiledResource } from "./tiled-resource";
import { Tileset } from "./tileset";
export declare class ObjectLayer implements Layer {
    tiledObjectLayer: TiledObjectLayer;
    resource: TiledResource;
    readonly order: number;
    private logger;
    readonly name: string;
    readonly class?: string;
    properties: Map<string, string | number | boolean>;
    objects: PluginObject[];
    entities: Entity[];
    private _objectToEntity;
    private _entityToObject;
    private _loaded;
    constructor(tiledObjectLayer: TiledObjectLayer, resource: TiledResource, order: number);
    private _logLoadedWarning;
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
    getTemplates(): TemplateObject[];
    /**
     * Runs or re-runs a specific registered factory given a class name on this object layer
     * @param className
     */
    runFactory(className: string): void;
    _actorFromObject(object: PluginObject, newActor: Actor, tileset?: Tileset): void;
    load(): Promise<void>;
    private _recordObjectEntityMapping;
}
