import React, {useState} from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import ApiService from "../ApiService";
import {routes} from "../App";
import {useSnackbar} from "notistack";
import TextField from "@material-ui/core/TextField";
import PortSelect from "./PortSelect";
import {FormControl, InputLabel, MenuItem, Select, Typography} from "@material-ui/core";
import {Link, useHistory} from "react-router-dom";
import {ShipType} from "../models/Ship";
import translations from "../translations";
import useError from "../useError";
import ResourcesModel from "../models/Resources";
import Resources from "./Resources";
import TeamOverview from "./TeamOverview";
import {teamId} from "../userContext";
import {useGameData} from "../gameDataContext";

interface BuyNewShipProps {
}

// Temporal workaround. Ship cost data is not fetched from BE but stored here.
const ShipCosts: {
    [key in ShipType]: ResourcesModel
} = {
    Brig: new ResourcesModel(2000, 150, 0, 0, 200),
    Frigate: new ResourcesModel(4000,200, 130, 0, 300),
    Galleon: new ResourcesModel(8000,250, 250, 0, 400),
    Schooner: new ResourcesModel(1000, 100, 0, 0, 100)
}

const BuyNewShip: React.FC<BuyNewShipProps> = (props) => {
    const {enqueueSnackbar} = useSnackbar();
    const [port, setPort] = useState("Port Royal");
    const [shipName, setShipName] = useState("");
    const [shipType, setShipType] = useState<ShipType>("Schooner");
    const {data, invalidateData} = useGameData();

    const history = useHistory();
    const {showErrorFromEvent} = useError();

    const handleFormSubmit = (e: any) => {
        e.preventDefault();
        ApiService.buyNewShip(shipName, port, shipType)
            .then(() => {
                enqueueSnackbar(`Loď ${shipName} byla zakoupena`, {variant: "success"});
                invalidateData();
                history.push(routes.overview);
            })
            .catch(showErrorFromEvent);
    };

    const teamData = data.enrichedGame?.game.teams.find(t => t.id === teamId()) ?? null;

    return (
        <form onSubmit={handleFormSubmit}>
            <Grid container direction="column" spacing={3}>
                <Grid item><TeamOverview team={teamData} createShips={false}/></Grid>
                <Grid item>
                    <Typography variant="h4">Nákup lodi</Typography>
                </Grid>
                <Grid item>
                    <TextField
                        fullWidth
                        variant="outlined"
                        label="Jméno lodi"
                        value={shipName}
                        onChange={e => setShipName(e.currentTarget.value)}
                    />
                </Grid>
                <Grid item>
                    <FormControl
                        variant="outlined"
                        fullWidth
                    >
                        <InputLabel id="ship-type-label">Typ lodi</InputLabel>
                        <Select
                            fullWidth
                            value={shipType ?? ""}
                            labelId="ship-type-label"
                            onChange={e => setShipType(e.target.value as ShipType)}
                            style={{minWidth: "16ch"}}
                            variant="outlined"
                            label="Typ lodi"
                        >
                            {["Schooner", "Brig", "Frigate", "Galleon"].map((type: string) => (
                                <MenuItem key={type} value={type}>{
                                    // @ts-ignore
                                    translations[type]
                                }</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item>
                    <PortSelect port={port} onPortSelected={port => setPort(port)}/>
                </Grid>
                <Grid item>
                    <Typography>cena: <Resources resources={ShipCosts[shipType]} hideZero /></Typography>
                </Grid>
                <Grid item>
                    <Typography variant="body2">
                        Loď bude k dispozici okamžitě. TODO pravidla možná nemůžete hrát rovnou s ní pls.
                    </Typography>
                </Grid>
                <Grid item>
                    <Grid container direction="row-reverse" spacing={2}>
                        <Grid item>
                            <Button
                                variant="contained"
                                color="primary"
                                type="submit"
                            >
                                Zakoupit loď
                            </Button>
                        </Grid>
                        <Grid item>
                            <Button component={Link} to={routes.overview} variant="outlined">Zpět</Button>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
        </form>
    );
}

export default BuyNewShip;