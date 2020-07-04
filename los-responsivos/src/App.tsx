import React from 'react';
import './App.css';
import TeamOverview from "./components/TeamOverview";
import {Box, Container, Grid} from '@material-ui/core';
import AllIcons from "./components/icons/AllIcons";


function App() {
    return (
        <Box paddingTop={3} paddingBottom={3}>
            <Container maxWidth={"sm"}>
                <TeamOverview/>
                {/*<AllIcons />*/}
            </Container>
        </Box>
    );
}

export default App;
