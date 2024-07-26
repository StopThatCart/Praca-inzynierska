export type PathMap = {
    path: string | RegExp;
    output: string;
}[];
export declare function filenameFromPath(inputPath: string): string;
export declare function mapPath(inputPath: string, pathMap: PathMap): string;
export declare function pathInMap(inputPath: string, pathMap?: PathMap): boolean;
export declare function pathRelativeToBase(basePath: string, relativeToBase: string, pathMap?: PathMap): string;
