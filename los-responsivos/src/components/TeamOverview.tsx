import React, {useEffect, useState} from "react";
import Team from "../models/Team";
import ApiService from "../ApiService";
import Resources from "./Resources";
import ResourcesModel from "../models/Resources";
import {CircularProgress, Grid, Typography} from "@material-ui/core";
import Ship from "./ships/Ship";

interface TeamOverviewProps {

}

type MaybeData = { loaded: true, team: Team } | { loaded: false, team: undefined }

const TeamOverview: React.FC<TeamOverviewProps> = (props) => {
    const [data, setData] = useState<MaybeData>({loaded: false, team: undefined});

    useEffect(() => {
        ApiService.getTeamData()
            .then(team => setData({loaded: true, team}));
    }, []);

    if (!data.loaded)
        return <CircularProgress/>;
    const team: Team = data.team;

    return (
        <Grid container direction={"column"} spacing={2}>
            <Grid item>
                <Typography variant={"h4"}>{team.name}</Typography>
            </Grid>
            <Grid item>
                <Resources resources={ResourcesModel.fromTeam(team)}/>
            </Grid>
            {team.ships.map(ship => (
                <Grid item>
                    <Ship key={ship.id} data={ship} />
                </Grid>
            ))}
        </Grid>
    );
}

export default TeamOverview;