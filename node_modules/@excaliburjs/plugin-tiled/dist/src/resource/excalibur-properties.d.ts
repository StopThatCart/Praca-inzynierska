/**
 * Special excalibur properties
 */
export declare const ExcaliburTiledProperties: {
    readonly TileData: {
        readonly Tiled: "ex-tiled";
    };
    readonly ZIndex: {
        /**
         * Override the default z-index based on the ordering in Tiled
         */
        readonly ZIndex: "zindex";
    };
    readonly Camera: {
        /**
         * Boolean property on an object to treat as the excalibur camera if truthy
         *
         * Warning, the plugin will use the first object it finds with this property!
         */
        readonly Camera: "camera";
        readonly Zoom: "zoom";
    };
    readonly Animation: {
        /**
         * String property with any value from the ex.AnimationStrategy enum (case insensitive)
         */
        readonly Strategy: "animationstrategy";
    };
    readonly Layer: {
        /**
         * Boolean property on a TileLayer, if truthy any tile on the layer will be treated as solid
         *
         * Boolean property on a ObjectLayer, if truthy any object will be created as an actor, objects
         * will have a default collision type of preventcollision, see the collisiontype property.
         */
        readonly Solid: "solid";
    };
    readonly Collision: {
        /**
         * String property with any value from the ex.CollisionType enum (case insensitive)
         */
        readonly Type: "collisiontype";
    };
};
