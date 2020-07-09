import React from 'react';
import './App.css';
import TeamOverview from "./components/TeamOverview";
import {Box, Container} from '@material-ui/core';
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import AllIcons from "./components/icons/AllIcons";
import ShipDetail from "./components/ships/ShipDetail";
import EdgeOfBreakfast from "./EdgeOfBreakfast";
import LoginForm from "./components/LoginForm";
import {useUser} from "./UserContext";

export const routes = {
    team: "/team",
    ship: "/ship",
    icons: "/icons",
    edgeOfBreakfast: "/edgeOfBreakfast",
    login: "/login",
}

function App() {
    const {user} = useUser();
    console.log("App, user:", user);
    return (
        <Box paddingTop={3} paddingBottom={3}>
            <Container maxWidth={"sm"}>
                <BrowserRouter>
                    <Switch>
                        <Route exact path={routes.login} component={LoginForm}/>
                        {user === null ? <Redirect from="/" to={routes.login}/> : null}
                        <Redirect exact from="/" to={routes.team}/>
                        <Route path={routes.team} component={TeamOverview}/>
                        <Route path={routes.ship + "/:id"} render={({match}) => <ShipDetail id={match.params.id}/>}/>
                        <Route path={routes.icons} component={AllIcons}/>
                        <Route path={routes.edgeOfBreakfast} component={EdgeOfBreakfast}/>
                        {/*default: */}
                        <Route render={() => <h1>404: stránka nenalezena</h1>}/>
                    </Switch>
                </BrowserRouter>
            </Container>
        </Box>
    );
}

export default App;
