export interface CanLoad {
    load(): Promise<any>;
}
/**
 * Read through cache for loadables
 */
export declare class LoaderCache<T extends CanLoad> {
    readonly type: new (...args: any[]) => T;
    private _loaded;
    cache: Map<string, T>;
    constructor(type: new (...args: any[]) => T);
    getOrAdd(...args: any[]): T;
    values(): T[];
    load(): Promise<void>;
}
