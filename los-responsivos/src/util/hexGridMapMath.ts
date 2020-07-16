import HexPosition from "../models/HexPosition";

export type Coordinate2D = { x: number, y: number }

export const mapImageWidth = 2500; // hardcoded value, width of the image from the backend
export const mapImageHeight = 2211; // hardcoded value, height of the image from the backend
export const SQRT_3 = Math.sqrt(3);
export const edgeLength = 107.312; // hardcoded value, length of the edge in the hardcoded image

export const hexToPixel = ({Q, R}: HexPosition): Coordinate2D => {
    return {
        x: edgeLength * SQRT_3 * (Q + R / 2.0),
        y: edgeLength * 3.0 / 2 * R
    };
}
export const pixelToHex = function (x: number, y: number): HexPosition {
    const Q = (x * SQRT_3 / 3 - y / 3) / edgeLength;
    const R = y * 2 / 3 / edgeLength;

    return hexRound(Q, R);
}
const hexRound = function (x: number, z: number): HexPosition {
    const y = -x - z;

    let rx = Math.round(x);
    let rz = Math.round(z);
    let ry = Math.round(y);

    const x_diff = Math.abs(rx - x);
    const y_diff = Math.abs(ry - y);
    const z_diff = Math.abs(rz - z);

    if (x_diff > y_diff && x_diff > z_diff) {
        rx = -ry - rz;
    } else if (y_diff <= z_diff) {
        rz = -rx - ry;
    }

    return {Q: rx, R: rz};
}
