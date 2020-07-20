import React from "react";
import {useGameData} from "../gameDataContext";
import Tile from "../models/Tile";
import {Divider, FormControl, Grid, InputLabel, MenuItem, Select, Typography} from "@material-ui/core";
import TileProximityView from "./TileProximityView";

interface PortSelectProps {
    port: string;
    onPortSelected: (newPort: string) => void;
}

const PortSelect: React.FC<PortSelectProps> = ({port, onPortSelected}) => {
    const {data} = useGameData();

    const allPorts: Array<Tile> = data.game?.map.tiles.filter(t => t.content === "Port") ?? [];
    return (
        <FormControl
            variant="outlined"
            fullWidth
        >
            <InputLabel id="PortSelect">Přístav</InputLabel>
            <Select
                value={port}
                onChange={e => onPortSelected(e.target.value as string)}
                renderValue={value => value as string}
                labelId="PortSelect"
                label="Přístav"
                variant="outlined"
            >
                {allPorts.map(port => (
                    <MenuItem key={port.portName as string} value={port.portName as string}>
                        <Grid container direction="column" spacing={0} style={{paddingBottom: 16}}>
                            <Grid item>
                                <Typography variant="h6">{port.portName}</Typography>
                            </Grid>
                            <Grid item><TileProximityView center={port.location} padding={0}/></Grid>
                        </Grid>
                        <Divider/>
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );
}

export default PortSelect;