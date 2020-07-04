import React, {useEffect, useState} from "react";
import {CircularProgress, Grid} from "@material-ui/core";
import "./Ship.css";
import ApiService from "../../ApiService";
import ShipDetailModel, {ShipAction } from "../../models/ShipDetail";
import Ship from "./Ship";
import Action from "../actions/Action";

interface ShipDetailProps {
    id: string
}

const allPossibleActions: Array<ShipAction> = Object.keys(ShipAction) as Array<ShipAction>;

type MaybeData = { loaded: true, ship: ShipDetailModel } | { loaded: false, ship: undefined }

const ShipDetail: React.FC<ShipDetailProps> = ({id}) => {
    const [data, setData] = useState<MaybeData>({loaded: false, ship: undefined});

    useEffect(() => {
        ApiService.getShipDetail(id)
            .then(ship => setData({loaded: true, ship}));
    }, [id]);

    if (!data.loaded)
        return <CircularProgress/>;
    const ship: ShipDetailModel = data.ship;

    return (
        <>
            <Ship data={ship} />
            Planned:
            <Grid container direction="row">
                {ship.plannedActions.map(action => (
                    <Grid item>
                        <Action planned text={action} />
                    </Grid>
                ))}
            </Grid>
            Plan:
            <Grid container direction="row">
                {allPossibleActions.map(action => (
                    <Grid item>
                        <Action text={action} available={ship.availableActions.get(action) ?? false} />
                    </Grid>
                ))}
            </Grid>
        </>

    )
}

export default ShipDetail;