import React from "react";
import ShipModel from "../models/Ship";
import {Grid, Paper} from "@material-ui/core";
import Resources from "./Resources";
import ResourcesModel from "./../models/Resources";
import ShipIcon from "./icons/ShipIcon";
import translations from "../translations";
import simpleship from "../assets/simpleship.svg";
import "./Ship.css";

interface ShipProps {
    data: ShipModel
}

const Ship: React.FC<ShipProps> = (props) => {
    const s = props.data;
    return (
        <Paper>
            <Grid container direction="row" spacing={3}>
                <Grid item>
                    <img src={simpleship} style={{width: 64, height: 64}} className={`deg-${s.orientationDeg}`} />
                </Grid>
                <Grid item>
                    <Grid container direction="column">
                        <Grid item>
                            <Grid container direction="row" spacing={1}>
                            <Grid item><ShipIcon/></Grid>
                            <Grid item>{translations[s.type]}</Grid>
                            <Grid item>{s.name}</Grid>
                            <Grid item>{s.captain}</Grid>
                            </Grid>
                        </Grid>
                        <Grid item>
                            <Resources resources={ResourcesModel.fromShip(s)}/>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>


        </Paper>
    );
}

export default Ship;