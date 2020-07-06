import React from 'react';
import './App.css';
import TeamOverview from "./components/TeamOverview";
import {Box, Container} from '@material-ui/core';
import {BrowserRouter, Route, Switch, Redirect} from "react-router-dom";
import AllIcons from "./components/icons/AllIcons";
import ShipDetail from "./components/ships/ShipDetail";
import EdgeOfBreakfast from "./EdgeOfBreakfast";

export const routes = {
    team: "/team",
    ship: "/ship",
    icons: "/icons",
    edgeOfBreakfast: "/edgeOfBreakfast",
}

function App() {
    return (
        <Box paddingTop={3} paddingBottom={3}>
            <Container maxWidth={"sm"}>
                <BrowserRouter>
                    <Switch>
                        <Redirect exact from="/" to="/team" />
                        <Route path={routes.team} component={TeamOverview}/>
                        <Route path={routes.ship + "/:id"} render={({match}) => <ShipDetail id={match.params.id} />} />
                        <Route path={routes.icons} component={AllIcons}/>
                        <Route path={routes.edgeOfBreakfast} component={EdgeOfBreakfast}/>
                        <Route render={() => <h1>404: stránka nenalezena</h1>} />
                    </Switch>
                </BrowserRouter>
            </Container>
        </Box>
    );
}

export default App;
