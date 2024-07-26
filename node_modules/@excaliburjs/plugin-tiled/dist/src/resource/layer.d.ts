import { Properties } from "./properties";
export interface Layer extends Properties {
    /**
     * Name from Tiled
     */
    name: string;
    /**
     * Original ordering from Tiled
     */
    order: number;
    /**
     * Class name from Tiled
     */
    class?: string;
    /**
     * Loads friendly datastructure (called by the plugin)
     * @internal
     */
    load(): Promise<void>;
}
