import React, {useEffect, useState} from "react";
import {Button, CircularProgress, Grid, IconButton, makeStyles, Tooltip, Typography} from "@material-ui/core";
import "./Ship.css";
import ApiService from "../../ApiService";
import ShipDetailModel from "../../models/ShipDetail";
import Ship from "./Ship";
import PlannedActions from "./actions/PlannedActions";
import {Link} from "react-router-dom";
import {routes} from "../../App";
import ActionPlanner from "./actions/ActionPlanner";
import DeleteForeverOutlinedIcon from '@material-ui/icons/DeleteForeverOutlined';
import useError from "../../useError";
import TileProximityView from "../TileProximityView";
import Resources from "../Resources";
import TeamOverview from "../TeamOverview";
import {useGameData} from "../../gameDataContext";

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
    const game = useGameData().data.enrichedGame;
    const {invalidateData, gameDataVersion} = useGameData();

    const [data, setData] = useState<MaybeData>({loaded: false, ship: undefined});

    useEffect(() => {
        ApiService.getShipDetail(id)
            .then(ship => setData({loaded: true, ship}))
            .catch(showErrorFromEvent);
    }, [id, gameDataVersion, showErrorFromEvent]);

    if (!data.loaded)
        return <div style={{textAlign: "center"}}><CircularProgress/></div>;
    const shipDetail: ShipDetailModel = data.ship;

    const handlePlannedActionClick = (index: number) => {
        ApiService.deleteActions(id, index + 1)
            .then(invalidateData)
            .catch(showErrorFromEvent);
    }

    const shipTeam = game?.game.teams.filter(x => x.id === shipDetail.ship.teamId)[0];

    return (
        <Grid container direction="column" spacing={2}>
            <Grid item>
                <Button component={Link} to={routes.overview} color="primary" variant="contained">Zpět na přehled</Button>
            </Grid>
            {shipTeam ? <Grid item><TeamOverview team={shipTeam} createShips={false}/></Grid> : null}
            <Grid item><Ship data={shipDetail.ship} clickable={false}/></Grid>
            <Grid item>
                <Typography variant="h6">Naplánované akce
                    <Tooltip title="Zrušit všechny plánované akce">
                        <IconButton onClick={() => handlePlannedActionClick(-1)} style={{float: "right"}}>
                            <DeleteForeverOutlinedIcon/>
                        </IconButton>
                    </Tooltip>
                </Typography>
                <PlannedActions actions={shipDetail.plannedActions} onActionClick={handlePlannedActionClick}/>
            </Grid>
            <Grid item>
                <Typography variant="h6">Naplánovat akce</Typography>
                <ActionPlanner
                    ship={shipDetail.ship}
                    plannableActions={shipDetail.plannableActions}
                    visibleActions={shipDetail.visibleActions}
                    onActionPlannedOk={invalidateData}
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