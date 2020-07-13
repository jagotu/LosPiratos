import React from 'react';
import './App.css';
import {Box, Container} from '@material-ui/core';
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import AllIcons from "./components/icons/AllIcons";
import ShipDetail from "./components/ships/ShipDetail";
import LoginForm from "./components/LoginForm";
import {useUser} from "./UserContext";
import GameOverview from "./components/GameOverview";

export const routes = {
    overview: "/overview",
    ship: "/ship",
    icons: "/icons",
    login: "/login",
    map: "/map",
    combatLog: "/combatLog",
    createShip: "/createShip",
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
                        <Route path={routes.ship + "/:id"} render={({match}) => <ShipDetail id={match.params.id}/>}/>
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
