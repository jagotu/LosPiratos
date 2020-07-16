import React from "react";
import {Link, useHistory} from "react-router-dom";
import {routes} from "../App";
import {Button} from "@material-ui/core";
import {mapImageHeight, mapImageWidth, pixelToHex} from "../util/hexGridMapMath";

interface MapViewProps {
}

const MapView: React.FC<MapViewProps> = (props) => {
    const history = useHistory();

    const handleImgOnClick = function (e: any): void {
        const x = e.nativeEvent.offsetX; //x position within the element.
        const y = e.nativeEvent.offsetY;  //y position within the element.

        // console.log(x - imageWidth / 2, y - imageHeight / 2);
        const position = pixelToHex(x - mapImageWidth / 2, y - mapImageHeight / 2);
        history.push(routes.factory.tileDetail(position));
    }

    return (
        <div>
            <Button component={Link} to={routes.overview} color="primary" variant="contained">Zpět</Button>
            <div style={{
                transform: "scale(0.3)",
                transformOrigin: "0 0",
                width: 750 + 8,
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