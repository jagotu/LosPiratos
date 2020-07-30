import React from "react";
import Team from "../models/Team";
import Resources from "./Resources";
import ResourcesModel from "../models/Resources";
import {Box, Grid, makeStyles, Typography} from "@material-ui/core";
import Ship from "./ships/Ship";
import getContrastColor from "../util/contrast";
import AnimateHeight from 'react-animate-height';

interface TeamOverviewProps {
    team: Team;
    isCurrentTeam?: boolean;
    areShipsVisible?: boolean;
    onClick?: () => void;
}

const useStyles = makeStyles(() => ({
    border: {
        borderStyle: "solid",
        borderRadius: 12,
        borderWidth: 4
    },
    header: {
        fontWeight: "bold",
        lineHeight: "30px",
        paddingLeft: 8,
        paddingRight: 0
    },
    content: {
        padding: "8px 8px"
    }
}));

const TeamOverview: React.FC<TeamOverviewProps> = (props) => {

    const classes = useStyles();
    const team = props.team;
    const isCurrentTeam = props.isCurrentTeam;
    const areShipsVisible = props.areShipsVisible ?? false;


    return (
        <div onClick={props.onClick} className={classes.border} style={{borderColor: team.color}}>
            <div className={classes.header}
                 style={{backgroundColor: team.color, color: getContrastColor(team.color)}}>
                <Typography style={{paddingBottom: 0}} variant={"h4"}>{team.name}</Typography>
            </div>
            <Grid container direction={"column"} spacing={1} className={classes.content}>
                <Grid item>
                    <Box paddingLeft={1}>
                        <Grid container direction="row" spacing={1}>
                            <Grid item><Resources resources={ResourcesModel.fromTeam(team)}/></Grid>
                            <Grid item><span className="icon">D</span> {team.ships.length}</Grid>
                        </Grid>
                    </Box>
                </Grid>
                <Grid item>
                    <AnimateHeight
                        duration={500}
                        height={areShipsVisible ? "auto" : "0"}>
                        <Grid container direction={"column"} spacing={1}>
                            {team.ships.map(ship => (
                                <Grid item key={ship.id}>
                                    <Ship key={ship.id} data={ship} clickable={isCurrentTeam ?? false}/>
                                </Grid>
                            ))}
                        </Grid>
                    </AnimateHeight>
                </Grid>
            </Grid>
        </div>
    );
}

TeamOverview.defaultProps = {
    isCurrentTeam: false
}

export default TeamOverview;