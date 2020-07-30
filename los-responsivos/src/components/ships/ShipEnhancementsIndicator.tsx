import React from "react";
import {Grid} from "@material-ui/core";
import Ship from "../../models/Ship";
import '../icons/icons.css';
import './enhancements.css'

interface ShipEnhancementsIndicatorProps {
    ship: Ship
}

const ShipEnhancementsIndicator: React.FC<ShipEnhancementsIndicatorProps> = (props) => {

    const s = props.ship;

    return (
        <Grid container direction="row" spacing={1}>
            <Grid item><span className={"icon enhancement " + (s.enhancements.CannonUpgrade ?? "empty")}>G</span></Grid>
            <Grid item><span className={"icon enhancement " + (s.enhancements.ChainShot ?? "empty")}>H</span></Grid>
            <Grid item><span className={"icon enhancement " + (s.enhancements.HeavyShot ?? "empty")}>I</span></Grid>
            <Grid item><span className={"icon enhancement " + (s.enhancements.HullUpgrade ?? "empty")}>J</span></Grid>
            <Grid item><span className={"icon enhancement " + (s.enhancements.Mortar ?? "empty")}>K</span></Grid>
            <Grid item><span className={"icon icon-lastminute enhancement " + (s.enhancements.Ram ?? "empty")} style={{fontWeight: "bold"}}>A</span></Grid>

        </Grid>
    );
}

export default ShipEnhancementsIndicator;