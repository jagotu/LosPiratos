import React, {useEffect, useState} from "react";
import {Button, CircularProgress, Grid, makeStyles} from "@material-ui/core";
import "./Ship.css";
import ApiService from "../../ApiService";
import ShipDetailModel from "../../models/ShipDetail";
import Ship from "./Ship";
import PlannedAction from "./actions/PlannedAction";
import {Link} from "react-router-dom";
import {routes} from "../../App";
import ActionPlanner from "./ActionPlanner";
import DeleteForeverOutlinedIcon from '@material-ui/icons/DeleteForeverOutlined';
import uid from "../../uid";

interface ShipDetailProps {
    id: string
}

const useStyles = makeStyles(() => ({
    plannedActionsContainer: {
        padding: 8,
        borderStyle: "solid",
        borderWidth: 1,
        borderColor: "#bebebe",
        borderRadius: 4,
        width: "max-content"
    }
}));

type MaybeData = { loaded: true, ship: ShipDetailModel } | { loaded: false, ship: undefined }

const ShipDetail: React.FC<ShipDetailProps> = ({id}) => {
    const [data, setData] = useState<MaybeData>({loaded: false, ship: undefined});
    const classes = useStyles();

    useEffect(() => {
        ApiService.getShipDetail(id)
            .then(ship => setData({loaded: true, ship}));
    }, [id]);

    if (!data.loaded)
        return <CircularProgress/>;
    const ship: ShipDetailModel = data.ship;

    const refreshData = () => {
      // TODO impl, nejak znovu nacist data
    };
    const removeActions = () => {
        ApiService.removeActions(id)
            .then(refreshData)
            .catch(e => {
               // TODO use notistack
            });
    };

    return (
        <Grid container direction="column" spacing={2}>
            <Grid item>
                <Button component={Link} to={routes.team} color="primary" variant="contained">Zpět</Button>
            </Grid>
            <Grid item><Ship data={ship}/></Grid>
            <Grid item>
                Planned:
                <div>
                    <div style={{float: "left", marginRight: 16}}>
                        <Button variant="contained" onClick={removeActions}>
                            <DeleteForeverOutlinedIcon/>
                        </Button>
                    </div>
                    <Grid container direction="row" className={classes.plannedActionsContainer}>
                        {ship.plannedActions.map(action => (
                            <Grid item key={uid()}>
                                <PlannedAction text={action}/>
                            </Grid>
                        ))}
                    </Grid>
                </div>
            </Grid>
            <Grid item>
                Plan:
                <ActionPlanner shipId={id} availableActions={ship.availableActions}/>
            </Grid>
        </Grid>

    )
}

export default ShipDetail;