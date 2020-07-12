import React from "react";
import ShipModel from "../../models/Ship";
import {Button, Grid, makeStyles} from "@material-ui/core";
import Resources from "../Resources";
import ResourcesModel from "../../models/Resources";
import ShipIcon from "../icons/ShipIcon";
import translations from "../../translations";
import simpleship from "../../assets/simpleship.svg";
import "./Ship.css";
import Position from "../Position";
import clsx from "clsx";
import {useHistory} from "react-router-dom";
import {routes} from "../../App";

interface ShipProps {
    data: ShipModel
}

const useStyles = makeStyles(() => ({
    shipButton: {
        paddingTop: 8,
        textTransform: "initial",
        width: "100%",
        justifyContent: "left"
    },
    shipView: {
        width: 64,
        height: 64,
        padding: 8
    }
}));

const Ship: React.FC<ShipProps> = (props) => {
    const s = props.data;
    const classes = useStyles();
    const history = useHistory();

    const handleClick = (): void => {
        history.push(`${routes.ship}/${s.id}`);
    };

    return (
        <Button className={classes.shipButton} onClick={handleClick} variant="outlined">
            <Grid container direction="column">
                <Grid item style={{display: "inline-flex"}}>
                    <div style={{float: "left"}}>
                        <img src={simpleship} className={clsx(`deg-${s.orientationDeg}`, classes.shipView)}/>
                    </div>
                    <div>
                        <Grid container direction="row" spacing={1} style={{alignItems: "center"}}>
                            <Grid item><ShipIcon/></Grid>
                            <Grid item>{translations[s.type]}</Grid>
                            <Grid item>{s.name}</Grid>
                            <Grid item>{s.captain}</Grid>
                            <Grid item><Position position={s.position}/></Grid>
                        </Grid>
                    </div>
                </Grid>
                <Grid item>
                    <Resources resources={ResourcesModel.fromShip(s)}/>
                </Grid>
            </Grid>
        </Button>
    );
}

export default Ship;