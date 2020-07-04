import React from "react";
import {Button, makeStyles} from "@material-ui/core";
import _ from "lodash";
import clsx from "clsx";

type ActionProps = {
    text: string;

} & ( {available: boolean, planned?: false} | {planned: true})

const size = 8*10;
const useStyles = makeStyles(() =>({
    button: {
        textTransform: "initial",
        margin: 8,
    },
    buttonPlannable: {
        width: size,
        height: size,
    },
}));

const Action: React.FC<ActionProps> = (props) => {
    const classes = useStyles();
    const appendText: string = props.planned ? " »" : "";

    return (
        <Button
            className={clsx(classes.button, {[classes.buttonPlannable]: props?.planned !== true})}
            disabled={props.planned ? false : !props.available}
            variant={props.planned ? "outlined" : "contained"}
        >
            {_.startCase(props.text) + appendText}
        </Button>
    );
}

export default Action;