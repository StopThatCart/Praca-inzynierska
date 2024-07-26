export declare const FLIPPED_HORIZONTALLY_FLAG = 2147483648;
export declare const FLIPPED_VERTICALLY_FLAG = 1073741824;
export declare const FLIPPED_DIAGONALLY_FLAG = 536870912;
/**
 * Inspects gid for horizontal flag
 * @param gid
 */
export declare const isFlippedHorizontally: (gid: number) => boolean;
/**
 * Inspects gid for vertical flag
 * @param gid
 */
export declare const isFlippedVertically: (gid: number) => boolean;
/**
 * Inspects gid for diagonal flag (anti-diagonal flip enables tile rotation)
 * @param gid
 */
export declare const isFlippedDiagonally: (gid: number) => boolean;
/**
 * Removes bit flags from gid
 * @param gid
 */
export declare const getCanonicalGid: (gid: number) => number;
