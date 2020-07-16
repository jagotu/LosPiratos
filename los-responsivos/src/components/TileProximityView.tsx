import React from "react";
import HexPosition from "../models/HexPosition";
import {Coordinate2D, edgeLength, hexToPixel, mapImageHeight, mapImageWidth, pixelToHex, SQRT_3} from "../util/hexGridMapMath";

interface GameTileProximityViewProps {
    center: HexPosition;
    /**
     * In material-ui units, i.e. 1 - 8
     */
    padding?: number;
    onTileSelected?: (tile: HexPosition) => void;
    /**
     * Styles applied to the underlying element.
     */
    style?: any;
    /**
     * Pass to force the inserted image to re-download every time the imgKey changes.
     */
    imgKey?: string;
    /**
     * whether to display fog on tiles that are farther than 2 from center
     */
    mortar?: boolean;
}

const tileWidth = SQRT_3 * edgeLength;
const tileHeight = edgeLength * 2;
const viewportWidth = tileWidth * 2;
const viewportHeight = tileHeight * 2;


const imgStyles = (cropCenter: Coordinate2D) => ({
    marginLeft: -(mapImageWidth / 2 + cropCenter.x - tileWidth / 2.0) + viewportWidth,
    marginTop: -(mapImageHeight / 2 + cropCenter.y - tileHeight / 2.0) + viewportHeight,
    transitionProperty: "margin",
    transitionDuration: "1s",
    transitionTimingFunction: "ease"

})
const containerStyles = {
    width: tileWidth + 2 * viewportWidth,
    height: tileHeight + 2 * viewportHeight,
    overflow: "hidden",
    borderRadius: 16
}

const TileProximityView: React.FC<GameTileProximityViewProps> = (props) => {
    const paddingCoeff = props.padding ?? 1;

    const handleImgOnClick = function (e: any): void {
        const x = e.nativeEvent.offsetX; //x position within the element.
        const y = e.nativeEvent.offsetY;  //y position within the element.

        // console.log(x - imageWidth / 2, y - imageHeight / 2);
        const position = pixelToHex(x - mapImageWidth / 2, y - mapImageHeight / 2);
        props.onTileSelected?.(position);
    }

    return (
        <div style={{padding: paddingCoeff * 8, ...props?.style}}>
            <div style={{
                transform: "scale(0.3)",
                transformOrigin: "0 0",
                width: 278, // hardcoded value, width after scaling
                height: 321 // hardcoded value, width after scaling
            }}>
                <div style={containerStyles}>
                    <img
                        style={{position: "absolute", left: 365, top: 423, pointerEvents: "none"}}
                        src={process.env.PUBLIC_URL + "/hexGridBorder.png"}
                    />
                    {props.mortar ?
                        <img
                            style={{position: "absolute", pointerEvents: "none"}}
                            src={process.env.PUBLIC_URL + "/mortar_scope.png"}
                        /> : null
                    }
                    <img
                        onClick={handleImgOnClick}
                        style={imgStyles(hexToPixel(props.center))}
                        src={process.env.REACT_APP_BACKEND_URL + `/map.jpg?${props.imgKey}`} // Append uid to prevent browser from caching
                        alt="Výřez herní mapy"
                    />
                </div>
            </div>
        </div>
    );
}

TileProximityView.defaultProps = {
    mortar: false
}


export default TileProximityView;