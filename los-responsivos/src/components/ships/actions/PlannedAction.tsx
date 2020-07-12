import React from "react";
import {makeStyles} from "@material-ui/core";
import _ from "lodash";
import {actionTranslations} from "./actionDetails";
import {ShipAction} from "../../../models/ShipActions";

interface PlannedActionProps {
    action: ShipAction;
}

const useStyles = makeStyles(() => ({
    root: {
        // margin: 8,
    }
}));


const PlannedAction: React.FC<PlannedActionProps> = ({action, ...props}) => {
    const classes = useStyles();

    const translation = actionTranslations.get(action);
    const label = translation ?? _.startCase(action);
    return (
        <div className={classes.root}
        >
            {label + " » "}
        </div>
    );
}

export default PlannedAction;