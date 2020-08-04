import React, {useEffect} from "react";
import {Button, CircularProgress, Divider, Grid, Typography} from "@material-ui/core";
import Game from "../models/Game";
import {useUser} from "../userContext";
import {Link} from "react-router-dom";
import {routes} from "../App";
import TeamOverview from "./TeamOverview";
import {useGameData} from "../gameDataContext";
import Team from "../models/Team";
import ReadyCheck from "./ReadyCheck";
import ApiService from "../ApiService";

const oneHourInMillis = 60 * 60 * 1000;

const GameOverview: React.FC = () => {
    const {teamId} = useUser();
    const {data, gameDataVersion} = useGameData();

    const [teamEnlarged, setTeamEnlarged] = React.useState<string>("")
    const [roundEndTime, setRoundEndTime] = React.useState(0); // unix timestap
    const [nowTime, setNowTime] = React.useState(Date.now()); // unix timestap

    useEffect(() => {
        ApiService.getRoundEndTime()
                .then(setRoundEndTime)
        },[gameDataVersion]
    );
    useEffect(() => {
        const timer = setInterval(() => {
            setNowTime(Date.now());
        },500);
        return () => clearInterval(timer);
    },[])

    if (!data.loaded)
        return <div style={{textAlign: "center"}}><CircularProgress/></div>;

    const game: Game = data.enrichedGame.game;
    const myTeam = game.teams.filter(t => t.id === teamId)[0];
    const otherTeams = game.teams.filter(t => t.id !== teamId);

    const controlPanel = (
        <Grid container direction="row" spacing={1}>
            <Grid item><Button component={Link} to={routes.buyShip} variant="contained">Nákup lodi</Button></Grid>
            <Grid item><Button component={Link} to={routes.map} variant="contained">Mapa</Button></Grid>
            <Grid item><Button component={Link} to={routes.combatLog} variant="contained">Combat log</Button></Grid>
            <Grid item><Button component={Link} to={routes.gameRules} variant="contained">Pravidla</Button></Grid>
        </Grid>
    )

    const onTeamClick = function (t: Team): void {
        if (teamEnlarged === t.id) {
            setTeamEnlarged("");
        } else {
            setTeamEnlarged(t.id);
        }
    }

    const timer = (
        <>
            <div style={{float: "left"}}>
                <Typography>{data.enrichedGame.roundNo}. kolo</Typography>
            </div>
            <div style={{float: "right"}}>
                <Typography>
                    {new Date(roundEndTime - nowTime - oneHourInMillis).toLocaleTimeString().substr(3)}
                </Typography>
            </div>
        </>
    );

    return (
        <Grid container spacing={2} direction="column">
            <Grid item>{timer}</Grid>
            <Grid item><TeamOverview team={myTeam} isCurrentTeam areShipsVisible={true}/></Grid>
            <Grid item><Divider/></Grid>
            <Grid item>{controlPanel}</Grid>
            <Grid item><ReadyCheck/></Grid>
            <Grid item><Divider/></Grid>
            {otherTeams.map(t => (
                <Grid item key={t.id}><TeamOverview team={t} areShipsVisible={teamEnlarged === t.id} onClick={() => onTeamClick(t)}/></Grid>
            ))}
        </Grid>
    );
}

export default GameOverview;