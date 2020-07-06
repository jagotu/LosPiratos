import React from "react";
import {makeStyles} from "@material-ui/core";
import _ from "lodash";

interface PlannedActionProps {
    text: string;
}

const useStyles = makeStyles(() => ({
    root: {
        // margin: 8,
    }
}));


const PlannedAction: React.FC<PlannedActionProps> = (props) => {
    const classes = useStyles();

    return (
        <div className={classes.root}
        >
            {_.startCase(props.text) + " » "}
        </div>
    );
}

export default PlannedAction;