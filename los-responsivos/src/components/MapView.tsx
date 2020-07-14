import React from "react";
import HexPosition from "../models/HexPosition";
import {Link, useHistory} from "react-router-dom";
import {routes} from "../App";
import {Button} from "@material-ui/core";

interface MapViewProps {
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

const hexToPixel = ({Q, R}: HexPosition): Coordinate2D => {
    return {
        x: edgeLength * SQRT_3 * (Q + R / 2.0),
        y: edgeLength * 3.0 / 2 * R
    };
}
const pixelToHex = function (x: number, y: number): HexPosition {
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

const MapView: React.FC<MapViewProps> = (props) => {
    const history = useHistory();

    const handleImgOnClick = function (e: any): void {
        const x = e.nativeEvent.offsetX; //x position within the element.
        const y = e.nativeEvent.offsetY;  //y position within the element.

        // console.log(x - imageWidth / 2, y - imageHeight / 2);
        const position = pixelToHex(x - imageWidth / 2, y - imageHeight / 2);
        history.push(`${routes.tileDetail}/${position.Q},${position.R}`);
    }

    return (
        <div>
            <Button component={Link} to={routes.overview} color="primary" variant="contained">Zpět</Button>
            <div style={{
                transform: "scale(0.3)",
                transformOrigin: "0 0",
                width: 750+8,
                height: 663
            }}>
                <img
                    style={{margin: 32}}
                    onClick={handleImgOnClick}
                    src={process.env.REACT_APP_BACKEND_URL + "/map.jpg"}
                    alt="Herní mapa"
                />
            </div>
        </div>
    );
}


export default MapView;