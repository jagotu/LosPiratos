import React from "react";
import {Button, makeStyles} from "@material-ui/core";
import _ from "lodash";
import clsx from "clsx";
import ApiService from "../../../ApiService";

interface PlannableActionProps {
    text: string;
    available: boolean;
    onClick: () => void;
}

const size = 8*10;
const useStyles = makeStyles(() =>({
    button: {
        textTransform: "initial",
        margin: 8,
        width: size,
        height: size,
    }
}));

const PlannableAction: React.FC<PlannableActionProps> = (props) => {
    const classes = useStyles();


    return (
        <Button
            className={classes.button}
            disabled={!props.available}
            variant="contained"
            onClick={props.onClick}
        >
            {_.startCase(props.text)}
        </Button>
    );
}

export default PlannableAction;