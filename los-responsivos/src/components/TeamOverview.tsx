import React from "react";
import Team from "../models/Team";
import Resources from "./Resources";
import ResourcesModel from "../models/Resources";
import {Grid, makeStyles, Typography} from "@material-ui/core";
import Ship from "./ships/Ship";
import getContrastColor from "../util/contrast";
import AnimateHeight from 'react-animate-height';

interface TeamOverviewProps {
    team: Team;
    isCurrentTeam?: boolean;
    areShipsVisible?: boolean;
    onClick?: () => void;
    createShips?: boolean;
}

const useStyles = makeStyles(() => ({
    border: {
        borderStyle: "solid",
        borderRadius: 8,
        borderWidth: 1,
        overflow: "hidden"
    },
    header: {
        padding: 8,
        paddingLeft: 16
    },
    content: {
        padding: 8,
    }
}));

const TeamOverview: React.FC<TeamOverviewProps> = (props) => {

    const classes = useStyles();
    const team = props.team;
    const isCurrentTeam = props.isCurrentTeam;
    const areShipsVisible = props.areShipsVisible ?? false;
    const createShips = props.createShips ?? true;

    const ships = (
        <AnimateHeight
            duration={500}
            height={areShipsVisible ? "auto" : 0}>
            <Grid container direction={"column"} spacing={1}>
                {team.ships.map(ship => (
                    <Grid item key={ship.id}>
                        <Ship key={ship.id} data={ship} clickable={isCurrentTeam ?? false}/>
                    </Grid>
                ))}
            </Grid>
        </AnimateHeight>
    );

    return (
        <div onClick={props.onClick} className={classes.border} style={{borderColor: team.color}}>
            <div className={classes.header} style={{backgroundColor: team.color, color: getContrastColor(team.color)}}>
                <Typography variant={"h5"}>{team.name}</Typography>
            </div>
            <Grid container direction={"column"} spacing={2} className={classes.content}>
                <Grid item>
                    <Grid container direction="row" spacing={1} style={{paddingLeft: 8}}>
                        <Grid item><Resources resources={ResourcesModel.fromTeam(team)}/></Grid>
                        <Grid item><span className="icon">D</span> {team.ships.length}</Grid>
                    </Grid>
                </Grid>
                <Grid item>
                    {createShips ? ships : null}
                </Grid>
            </Grid>
        </div>
    );
}

TeamOverview.defaultProps = {
    isCurrentTeam: false
}

export default TeamOverview;