import React, {useMemo} from "react";
import {Link, useHistory} from "react-router-dom";
import {routes} from "../App";
import {Button, Grid, Typography} from "@material-ui/core";
import HexPosition from "../models/HexPosition";
import Position from "./Position";
import TileProximityView from "./TileProximityView";

interface TileDetailProps {
    /**
     * String as matched in the URL, i.e. in format "Q.R"
     */
    coordinates: string
}

const TileDetail: React.FC<TileDetailProps> = ({coordinates}) => {
    const position: HexPosition = useMemo(() => ({
        Q: parseInt(coordinates.split(",")[0]),
        R: parseInt(coordinates.split(",")[1]),
    }),[coordinates]);
    const history = useHistory();

    return (
        <>
            <Grid direction="row" spacing={1} >
                <Grid item>
                    <Button component={Link} to={routes.map} color="primary" variant="contained">Zpět na mapu</Button>
                </Grid>
                <Grid item>
                    <Button component={Link} to={routes.overview} color="primary" variant="contained">Přehled</Button>
                </Grid>
            </Grid>
            <Typography>Políčko <Position position={position} /></Typography>
            <TileProximityView
                onTileSelected={(position) => history.push(`${routes.tileDetail}/${position.Q},${position.R}`)}
                center={position}
            />
        </>
    );
}

export default TileDetail;