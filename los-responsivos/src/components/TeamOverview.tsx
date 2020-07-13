import React from "react";
import Team from "../models/Team";
import Resources from "./Resources";
import ResourcesModel from "../models/Resources";
import {Grid, Typography} from "@material-ui/core";
import Ship from "./ships/Ship";

interface TeamOverviewProps {
    team: Team;
    isCurrentTeam?: boolean;
}

const TeamOverview: React.FC<TeamOverviewProps> = ({team, isCurrentTeam}) => {

    return (
        <Grid container direction={"column"} spacing={1}>
            <Grid item>
                <Typography style={{paddingBottom: 0}} variant={"h4"}>{team.name}</Typography>
            </Grid>
            <Grid item>
                <Resources resources={ResourcesModel.fromTeam(team)}/>
            </Grid>
            {team.ships.map(ship => (
                <Grid item key={ship.id}>
                    <Ship key={ship.id} data={ship} clickable={isCurrentTeam ?? false}/>
                </Grid>
            ))}
        </Grid>
    );
}

TeamOverview.defaultProps = {
    isCurrentTeam: false
}

export default TeamOverview;