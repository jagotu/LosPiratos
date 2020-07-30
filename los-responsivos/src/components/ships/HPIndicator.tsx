import React from "react";
import {Box, Grid, LinearProgress, makeStyles, withStyles} from "@material-ui/core";

interface HPIndicatorProps {
    HP: number,
    maxHP: number
}

const maxIngameHPEver = 175;

const HPBar = withStyles((theme) => ({
    root: {
        height: 15,
        borderRadius: 5,
        borderWidth: 1,
        borderColor: "black",
        borderStyle: "solid"
    },
    colorPrimary: {
        backgroundColor: "white",
    },
    bar: {
        borderRadius: 5,
        backgroundColor: 'lime',
    },
}))(LinearProgress);

const useStyles = makeStyles(() => ({
    hpText: {
        lineHeight: "15px",
        paddingLeft: 8
    }
}));

const HPIndicator: React.FC<HPIndicatorProps> = (props) => {

    const classes = useStyles();

    return (
        <Grid container direction="row">
            <Box flexGrow={1} style={{position: "relative"}}>
                <div style={{width: `${props.maxHP / maxIngameHPEver * 100}%`}}>
                    <HPBar variant="determinate" value={100*props.HP/props.maxHP} />
                </div>
            </Box>
            <Box>
                <div className={classes.hpText}>{props.HP}/{props.maxHP}</div>
            </Box>
        </Grid>
    );
}

export default HPIndicator;