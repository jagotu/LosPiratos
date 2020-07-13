import React from "react";
import Position from "../models/Position";

interface GameTileProximityViewProps {
    center: Position,
    // in material-ui units, i.e. 1 - 8
    padding?: number
    onTileSelected?: (tile: Position) => void;
}

type Coordinate2D = { x: number, y: number }

const imageWidth = 2500; // hardcoded value, width of the image from the backend
const imageHeight = 2211; // hardcoded value, height of the image from the backend

const SQRT_3 = Math.sqrt(3);
const edgeLength = 107.312;
const tileWidth = SQRT_3 * edgeLength;
const tileHeight = edgeLength * 2;
const viewportWidth = tileWidth * 2;
const viewportHeight = tileHeight * 2;

const hexToPixel = ({Q, R}: Position): Coordinate2D => {
    return {
        x: edgeLength * SQRT_3 * (Q + R / 2.0),
        y: edgeLength * 3.0 / 2 * R
    };
}
const pixelToHex = function (x: number, y: number): Position {
    const Q = (x * SQRT_3 / 3 - y / 3) / edgeLength;
    const R = y * 2 / 3 / edgeLength;

    return hexRound(Q, R);
}
const hexRound = function (x: number, z: number): Position {
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

const imgStyles = (cropCenter: Coordinate2D) => ({
    marginLeft: -(imageWidth / 2 + cropCenter.x - tileWidth / 2.0) + viewportWidth,
    marginTop: -(imageHeight / 2 + cropCenter.y - tileHeight / 2.0) + viewportHeight
})
const containerStyles = {
    width: tileWidth + 2 * viewportWidth,
    height: tileHeight + 2 * viewportHeight,
    overflow: "hidden",
    borderRadius: 16
}

const GameTileProximityView: React.FC<GameTileProximityViewProps> = (props) => {
    const paddingCoeff = props.padding ?? 1;

    const handleImgOnClick = function (e: any): void {
        const x = e.nativeEvent.offsetX; //x position within the element.
        const y = e.nativeEvent.offsetY;  //y position within the element.

        // console.log(x - imageWidth / 2, y - imageHeight / 2);
        const result = pixelToHex(x - imageWidth / 2, y - imageHeight / 2);
        props.onTileSelected?.(result);
    }

    return (
        <div style={{padding: paddingCoeff * 8}}>
            <div style={{
                transform: "scale(0.3)",
                transformOrigin: "0 0",
                width: 278, // hardcoded value, width after scaling
                height: 321 // hardcoded value, width after scaling
            }}>
                <div style={containerStyles}>
                    <img
                        onClick={handleImgOnClick}
                        style={imgStyles(hexToPixel(props.center))}
                        src={process.env.PUBLIC_URL + "map.jpg"}
                        alt="Výřez herní mapy"
                    />
                </div>
            </div>
        </div>
    );
}


export default GameTileProximityView;