import React from 'react';
import './App.css';
import TeamOverview from "./components/TeamOverview";
import {Box, Container} from '@material-ui/core';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import AllIcons from "./components/icons/AllIcons";
import ShipDetail from "./components/ships/ShipDetail";

function App() {
    return (
        <Box paddingTop={3} paddingBottom={3}>
            <Container maxWidth={"sm"}>
                <BrowserRouter>
                    <Switch>
                        <Route path="/" exact component={TeamOverview}/>
                        <Route path="/ship/:id" render={({match}) => <ShipDetail id={match.params.id} />} />
                        <Route path="/icons" component={AllIcons}/>
                        <Route render={() => <h1>404: stránka nenalezena</h1>} />
                    </Switch>
                </BrowserRouter>
            </Container>
        </Box>
    );
}

export default App;
