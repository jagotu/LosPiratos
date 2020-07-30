import React from "react";
import {Box, Button, CircularProgress, Grid} from "@material-ui/core";
import Game from "../models/Game";
import {useUser} from "../userContext";
import {Link} from "react-router-dom";
import {routes} from "../App";
import TeamOverview from "./TeamOverview";
import {useGameData} from "../gameDataContext";
import Team from "../models/Team";

const GameOverview: React.FC = () => {
    const {teamId} = useUser();
    const {data} = useGameData();

    const [teamEnlarged, setTeamEnlarged] = React.useState<string>("")

    if (!data.loaded)
        return <div style={{textAlign: "center"}}><CircularProgress/></div>;

    const game: Game = data.enrichedGame.game;
    const myTeam = game.teams.filter(t => t.id === teamId)[0];
    const otherTeams = game.teams.filter(t => t.id !== teamId);


    const controlPanel = (
        <Grid container direction="row" spacing={1}>
            <Grid item><Button component={Link} to={routes.map} variant="contained">Mapa</Button></Grid>
            <Grid item><Button component={Link} to={routes.combatLog} variant="contained">Combat log</Button></Grid>
            <Grid item><Button component={Link} to={routes.buyShip} variant="contained">Nákup lodi</Button></Grid>
        </Grid>
    )

    const clickHandler = function (t: Team): void {
        if(teamEnlarged === t.id)
        {
            setTeamEnlarged("");
        } else {
            setTeamEnlarged(t.id);
        }
    }


    return (
        <Grid container spacing={3} direction="column">
            <Grid item><TeamOverview team={myTeam} isCurrentTeam areShipsVisible={true}/></Grid>
            <Grid item>{controlPanel}</Grid>
            <Box pb={2}/> {/* visual separator */}
            {otherTeams.map(t => (
                <Grid item key={t.id}><TeamOverview team={t} areShipsVisible={teamEnlarged === t.id} onClick={() => clickHandler(t)}/></Grid>
            ))}
        </Grid>
    );
}

export default GameOverview;