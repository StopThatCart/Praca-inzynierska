export type FileLoader = (path: string, contentType: 'json' | 'xml') => Promise<string | any>;
export declare const FetchLoader: FileLoader;
