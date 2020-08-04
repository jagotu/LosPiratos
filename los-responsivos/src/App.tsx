import React from 'react';
import './App.css';
import {Box, Container} from '@material-ui/core';
import {BrowserRouter, Redirect, Route, RouteProps, Switch} from "react-router-dom";
import AllIcons from "./components/icons/AllIcons";
import ShipDetail from "./components/ships/ShipDetail";
import LoginForm from "./components/LoginForm";
import {useUser} from "./userContext";
import GameOverview from "./components/GameOverview";
import TileDetail from "./components/TileDetail";
import HexPosition from "./models/HexPosition";
import BuyNewShip from "./components/BuyNewShip";
import CombatLog from "./components/CombatLog";
import Pexeso from "./components/edgeOfBreakfast/Pexeso";
// @ts-ignore
import Websocket from 'react-websocket';
import {useGameData} from "./gameDataContext";
import GameRules from "./components/GameRules";
import {useSnackbar} from "notistack";

export const routes = {
    overview: "/overview",
    shipDetail: "/ship",
    tileDetail: "/tile",
    icons: "/icons",
    login: "/login",
    map: "/tile/0,0?fullMap",
    combatLog: "/combatLog",
    gameRules: "/gameRules",
    buyShip: "/buyShip",
    factory: {
        tileDetail: (position: HexPosition) => `${routes.tileDetail}/${position.Q},${position.R}`
    }
}

const renderTileDetail: RouteProps["render"] = ({location, match}) => (
    <TileDetail
        coordinates={match.params.coordinates}
        fullMap={location.search.includes("fullMap")}
    />
);

const EdgeOfBreakfast = false;

function App() {
    const {user} = useUser();
    const {invalidateData} = useGameData();
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const handleWebsocketMessage = (message: string) => {
        if (message === "refresh")
            invalidateData();
        if (message.startsWith("B:")) {
            enqueueSnackbar(message.substr(2), {
                variant: "info",
                persist: true,
                onClick: () => closeSnackbar()
            });
        }
    }

    if (EdgeOfBreakfast) {
        return (
            <Pexeso/>
        )
    } else return (
        <Box paddingTop={3} paddingBottom={3}>
            <Websocket url={process.env.REACT_APP_WEBSOCKET_URL} onMessage={handleWebsocketMessage}/>
            <Container maxWidth={"sm"}>
                <BrowserRouter>
                    <Switch>
                        <Route exact path={routes.login} component={LoginForm}/>
                        {user === null ? <Redirect from="/" to={routes.login}/> : null}
                        <Redirect exact from="/" to={routes.overview}/>
                        <Route path={routes.overview} component={GameOverview}/>
                        <Route path={routes.shipDetail + "/:id"}
                               render={({match}) => <ShipDetail id={match.params.id}/>}
                        />
                        <Route path={routes.tileDetail + "/:coordinates"} render={renderTileDetail}/>
                        <Route path={routes.buyShip} component={BuyNewShip}/>
                        <Route path={routes.icons} component={AllIcons}/>
                        <Route path={routes.combatLog} component={CombatLog}/>
                        <Route path={routes.gameRules} component={GameRules}/>
                        {/*default: */}
                        <Route render={() => <h1>404: stránka nenalezena</h1>}/>
                    </Switch>
                </BrowserRouter>
            </Container>
        </Box>
    );
}

export default App;
