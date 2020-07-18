import React from "react";
import HexPosition from "../models/HexPosition";
import {Coordinate2D, edgeLength, hexToPixel, mapImageHeight, mapImageWidth, pixelToHex, SQRT_3} from "../util/hexGridMapMath";
import {makeStyles} from "@material-ui/core/styles";
import clsx from "clsx";

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
    fullMap?: boolean;
}

const tileWidth = SQRT_3 * edgeLength;
const tileHeight = edgeLength * 2;
const viewportWidth = tileWidth * 2;
const viewportHeight = tileHeight * 2;

const styles = {
    mapImg: (cropCenter: Coordinate2D, fullMap: boolean) => ({
        marginLeft: fullMap ? "unset" : -(mapImageWidth / 2 + cropCenter.x - tileWidth / 2.0) + viewportWidth,
        marginTop: fullMap ? "unset" : -(mapImageHeight / 2 + cropCenter.y - tileHeight / 2.0) + viewportHeight,
    }),
}

const useStyles = makeStyles(() => ({
    mapContainer: {
        width: tileWidth + 2 * viewportWidth,
        height: tileHeight + 2 * viewportHeight,
        overflow: "hidden",
        borderRadius: 16,
        transition: "width 1s ease, height 1s ease",
    },
    mapContainerOriginalSize: {
        width: mapImageWidth + 56,
        height: mapImageHeight
    },
    mapImg: {
        transition: "margin 1s ease"
    },
    hidden: {
        visibility: "hidden"
    },
    transparent: {
        opacity: 0,
    },
    opacityTransition: {
        transition: "opacity 0.2s linear 1s",
    },
    detailCenter: {
        position: "absolute",
        left: 365, // hardcoded value, based on the image size tileWidth
        top: 423, // hardcoded value, based on the image size tileWidth
        pointerEvents: "none"
    },
    rootScaled: {
        transform: "scale(0.3)",
        transformOrigin: "0 0",
        width: 278, // hardcoded value, width after scaling
        height: 321, // hardcoded value, width after scaling
    },
    sizeMaxContent: {
        width: "max-content",
        height: "max-content"
    }
}));

const TileProximityView: React.FC<GameTileProximityViewProps> = (props) => {
    const classes = useStyles();
    const paddingCoeff = props.padding ?? 1;
    const fullMap = props.fullMap ?? false;

    const handleImgOnClick = function (e: any): void {
        const x = e.nativeEvent.offsetX; //x position within the element.
        const y = e.nativeEvent.offsetY;  //y position within the element.

        // console.log(x - imageWidth / 2, y - imageHeight / 2);
        const position = pixelToHex(x - mapImageWidth / 2, y - mapImageHeight / 2);
        props.onTileSelected?.(position);
    }

    return (
        <div style={{padding: paddingCoeff * 8, ...props?.style}}>
            <div className={clsx(classes.rootScaled, {[classes.sizeMaxContent]: fullMap})}>
                <div className={clsx(classes.mapContainer, {[classes.mapContainerOriginalSize]: fullMap})}>
                    <img
                        className={clsx(
                            classes.detailCenter,
                            {[classes.transparent]: fullMap},
                            {[classes.opacityTransition]: !fullMap}
                        )}
                        src={process.env.PUBLIC_URL + "/hexGridBorder.png"}
                        alt=""
                    />
                    <img
                        className={clsx({[classes.hidden]: !props.mortar})}
                        style={{position: "absolute", pointerEvents: "none"}}
                        src={process.env.PUBLIC_URL + "/mortar_scope.png"}
                        alt=""
                    />
                    <img
                        onClick={handleImgOnClick}
                        style={styles.mapImg(hexToPixel(props.center), fullMap)}
                        className={classes.mapImg}
                        src={process.env.REACT_APP_BACKEND_URL + `/map.jpg?${props.imgKey}`} // Append uid to prevent browser from caching
                        alt="Výřez herní mapy"
                    />
                </div>
            </div>
        </div>
    );
}

TileProximityView.defaultProps = {
    mortar: false,
    fullMap: false
}


export default TileProximityView;