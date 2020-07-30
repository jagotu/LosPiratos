import React, {useEffect, useState} from "react";
import {Button, CircularProgress, Grid, makeStyles, Typography} from "@material-ui/core";
import "./Ship.css";
import ApiService from "../../ApiService";
import ShipDetailModel from "../../models/ShipDetail";
import Ship from "./Ship";
import PlannedAction from "./actions/PlannedAction";
import {Link} from "react-router-dom";
import {routes} from "../../App";
import ActionPlanner from "./actions/ActionPlanner";
import DeleteForeverOutlinedIcon from '@material-ui/icons/DeleteForeverOutlined';
import uid from "../../util/uid";
import useError from "../../useError";
import TileProximityView from "../TileProximityView";
import Resources from "../Resources";

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
    }
}));

type MaybeData = { loaded: true, ship: ShipDetailModel } | { loaded: false, ship: undefined }

const ShipDetail: React.FC<ShipDetailProps> = ({id}) => {
    const classes = useStyles();
    const {showErrorFromEvent} = useError();

    const [data, setData] = useState<MaybeData>({loaded: false, ship: undefined});
    const [dataVersion, setDataVersion] = useState(0);

    useEffect(() => {
        ApiService.getShipDetail(id)
            .then(ship => setData({loaded: true, ship}))
            .catch(showErrorFromEvent);
    }, [id, dataVersion, showErrorFromEvent]);

    if (!data.loaded)
        return <div style={{textAlign: "center"}}><CircularProgress/></div>;
    const shipDetail: ShipDetailModel = data.ship;

    const refreshData = () => setDataVersion(oldValue => oldValue + 1); // force React to recall the ApiService
    const removeActions = () => {
        ApiService.deleteActions(id, 0)
            .then(refreshData)
            .catch(showErrorFromEvent);
    };

    return (
        <Grid container direction="column" spacing={2}>
            <Grid item>
                <Button component={Link} to={routes.overview} color="primary" variant="contained">Zpět na přehled</Button>
            </Grid>
            <Grid item><Ship data={shipDetail.ship} clickable={false}/></Grid>
            <Grid item>
                <Typography variant="h6">Naplánované akce</Typography>
                <div>
                    <div style={{float: "left", marginRight: 16}}>
                        <Button variant="contained" onClick={removeActions}>
                            <DeleteForeverOutlinedIcon/>
                        </Button>
                    </div>
                    <Grid container direction="row" className={classes.plannedActionsContainer}>
                        {shipDetail.plannedActions.map(action => (
                            <Grid item key={uid()}>
                                <PlannedAction action={action}/>
                            </Grid>
                        ))}
                    </Grid>
                </div>
            </Grid>
            <Grid item>
                <Typography variant="h6">Naplánovat akce</Typography>
                <ActionPlanner
                    ship={shipDetail.ship}
                    plannableActions={shipDetail.plannableActions}
                    visibleActions={shipDetail.visibleActions}
                    onActionPlannedOk={refreshData}
                />
            </Grid>
            <Grid item>Aktuální cena vylepšení: <Resources resources={shipDetail.upgradeCost} hideZero={true}/></Grid>
            <Grid item>Aktuální cena opravy: <Resources resources={shipDetail.repairCost} hideZero={true}/></Grid>
            <Grid item>
                <Typography variant="h6">Okolí lodi</Typography>
                <Link to={routes.factory.tileDetail(shipDetail.ship.position)}>
                    <TileProximityView center={shipDetail.ship.position}/>
                </Link>
            </Grid>
        </Grid>

    )
}

export default ShipDetail;