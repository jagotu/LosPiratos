import React, {useState} from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import ApiService from "../ApiService";
import {routes} from "../App";
import {useSnackbar} from "notistack";
import TextField from "@material-ui/core/TextField";
import PortSelect from "./PortSelect";
import {Typography} from "@material-ui/core";
import {Link, useHistory} from "react-router-dom";

interface BuyNewShipProps {
}

const BuyNewShip: React.FC<BuyNewShipProps> = (props) => {
    const {enqueueSnackbar} = useSnackbar();
    const [port, setPort] = useState("Port Royal");
    const [shipName, setShipName] = useState("");
    const [captainName, setCaptainName] = useState("");
    const history = useHistory();

    const handleFormSubmit = (e: any) => {
        e.preventDefault();
        ApiService.buyNewShip(shipName, captainName, port)
            .then(() => {
                enqueueSnackbar(`Loď ${shipName} byla zakoupena`,{variant: "success"});
                history.push(routes.overview);
            })
            .catch(e =>  {
                enqueueSnackbar("Ay! Něco není správně, loď nebyla vytvořena.",{variant: "error"});
            });
    };

    return (
        <form onSubmit={handleFormSubmit}>
            <Grid container direction="column" spacing={3}>
                <Grid item>
                    <Typography variant="h4">Nákup lodi</Typography>
                </Grid>
                <Grid item>
                    <TextField
                        fullWidth
                        variant="outlined"
                        label="jméno lodi"
                        value={shipName}
                        onChange={e => setShipName(e.currentTarget.value)}
                    />
                </Grid>
                <Grid item>
                    <TextField
                        fullWidth
                        variant="outlined"
                        label="jméno kapitána"
                        value={captainName}
                        onChange={e => setCaptainName(e.currentTarget.value)}
                    />
                </Grid>
                <Grid item>
                    <PortSelect port={port} onPortSelected={port => setPort(port)} />
                </Grid>
                <Grid item>
                    <Typography variant="body2">
                    Loď bude k dispozici na začátku příštího tahu. Pro pořízení lepšího druhu lodi zakupte Brigu a v příštím tahu loď vylepšete pomocí transakce.
                    </Typography>
                </Grid>
                <Grid item>
                    <Grid container direction="row" spacing={2}>
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