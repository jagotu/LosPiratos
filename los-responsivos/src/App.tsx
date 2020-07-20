import React from 'react';
import './App.css';
import {Box, Container} from '@material-ui/core';
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import AllIcons from "./components/icons/AllIcons";
import ShipDetail from "./components/ships/ShipDetail";
import LoginForm from "./components/LoginForm";
import {useUser} from "./userContext";
import GameOverview from "./components/GameOverview";
import TileDetail from "./components/TileDetail";
import MapView from "./components/MapView";
import HexPosition from "./models/HexPosition";
import BuyNewShip from "./components/BuyNewShip";

export const routes = {
    overview: "/overview",
    shipDetail: "/ship",
    tileDetail: "/tile",
    icons: "/icons",
    login: "/login",
    map: "/map",
    combatLog: "/combatLog",
    buyShip: "/buyShip",
    factory: {
        tileDetail: (position: HexPosition) => `${routes.tileDetail}/${position.Q},${position.R}`
    }
}

function App() {
    const {user} = useUser();
    return (
        <Box paddingTop={3} paddingBottom={3}>
            <Container maxWidth={"sm"}>
                <BrowserRouter>
                    <Switch>
                        <Route exact path={routes.login} component={LoginForm}/>
                        {user === null ? <Redirect from="/" to={routes.login}/> : null}
                        <Redirect exact from="/" to={routes.overview}/>
                        <Route path={routes.overview} component={GameOverview}/>
                        <Route path={routes.map} component={MapView}/>
                        <Route path={routes.shipDetail + "/:id"}
                               render={({match}) => <ShipDetail id={match.params.id}/>}
                        />
                        <Route path={routes.tileDetail + "/:coordinates"}
                               render={({match}) => <TileDetail coordinates={match.params.coordinates}/>}
                        />
                        <Route path={routes.buyShip} component={BuyNewShip}/>
                        <Route path={routes.icons} component={AllIcons}/>
                        {/*default: */}
                        <Route render={() => <h1>404: stránka nenalezena</h1>}/>
                    </Switch>
                </BrowserRouter>
            </Container>
        </Box>
    );
}

export default App;
